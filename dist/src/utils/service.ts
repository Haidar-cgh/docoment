import axios, {type AxiosInstance, type AxiosRequestConfig} from "axios"
import {ElMessage} from "element-plus"
import {get, merge} from "lodash-es"
import {getToken, setUuid} from "./cache/cookies"
import {useUserStore} from "@/store/modules/user"

/** 退出登录并强制刷新页面（会重定向到登录页） */
function logout() {
  return useUserStore().logout()
}

async function toJson(bob: Blob): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      resolve(<string>e.target?.result)
    }
    reader.onerror = (e) => {
      reject(e)
    }
    reader.readAsText(bob)
  })
}

const regex_reLogin = /^11[01][0-9][0-7|9]$/

/** 创建请求实例 */
function createService() {
  // 创建一个 axios 实例命名为 service
  const service = axios.create()
  // 请求拦截
  service.interceptors.request.use(
    (config) => {
      return config
    },
    // 发送失败
    (error) => {
      ElMessage.error(error.message)
    }
  )
  // 响应拦截（可根据具体业务作出相应的调整）
  service.interceptors.response.use(
    (response) => {
      // apiData 是 api 返回的数据
      const apiData = response.data
      // 二进制数据则直接返回
      const responseType = response.request?.responseType
      if (responseType === "blob" || responseType === "arraybuffer") {
        if (apiData.type != "image/jpeg") {
          return toJson(apiData).then((apiD) => {
            return JSON.parse(apiD)
          })
        } else {
          setUuid(response.headers.uuid)
          return apiData
        }
      }
      // 这个 code 是和后端约定的业务 code
      const code = Number(apiData.code || apiData?.status?.code)
      // 如果没有 code, 代表这不是项目后端开发的 api
      if (code === undefined) {
        ElMessage.error("非本系统的接口")
        return Promise.reject(new Error("非本系统的接口"))
      }
      if (regex_reLogin.test(code + "")) {
        return logout()
      }
      switch (code) {
        case 0:
          // 本系统采用 code === 0 来表示没有业务错误
          return apiData
        default:
          // 不是正确的 code
          ElMessage.error(apiData.message || "Error")
          return Promise.reject(new Error("Error"))
      }
    },
    (error) => {
      // status 是 HTTP 状态码
      const status = get(error, "response.status")
      switch (status) {
        case 400:
          error.message = "请求错误"
          break
        case 401:
          // Token 过期时
          logout()
          break
        case 403:
          error.message = "拒绝访问"
          break
        case 404:
          error.message = "请求地址出错"
          break
        case 408:
          error.message = "请求超时"
          break
        case 500:
          error.message = "服务器内部错误"
          break
        case 501:
          error.message = "服务未实现"
          break
        case 502:
          error.message = "网关错误"
          break
        case 503:
          error.message = "服务不可用"
          break
        case 504:
          error.message = "网关超时"
          break
        case 505:
          error.message = "HTTP 版本不受支持"
          break
        default:
          break
      }
      console.error(error.message)
      if (error.message == "Network Error") {
        ElMessage.error("服务异常")
      } else if (error.message && error.message.indexOf("timeout") !== -1) {
        ElMessage.error("请求超时")
      } else {
        return Promise.reject(error)
      }
    }
  )
  return service
}

/** 创建请求方法 */
function createRequest(service: AxiosInstance) {
  return function <T>(config: AxiosRequestConfig): Promise<T> {
    const token = getToken()
    const defaultConfig = {
      headers: {
        // 携带 Token
        Authorization: token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json;charset=utf-8"
        // multipart/form-data
        // application/x-www-form-urlencoded
        // application/xml
        // text/xml
      },
      timeout: import.meta.env.VITE_BASE_TIMEOUT,
      baseURL: import.meta.env.VITE_BASE_API,
      data: {}
    }
    // 将默认配置 defaultConfig 和传入的自定义配置 config 进行合并成为 mergeConfig
    const mergeConfig = merge(defaultConfig, config)
    return service(mergeConfig)
  }
}

/** 用于网络请求的实例 */
const service = createService()
/** 用于网络请求的方法 */
export const request = createRequest(service)
