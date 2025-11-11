import config from "@/config";
import http from "@/utils/request";

export default {
	getCodeEnable:{
		url: `${config.API_URL}/public/config/getCodeEnable`,
			name: "登录",
			get: async function(data={}){
			return await http.get(this.url, data);
		}
	},
	code: {
		url: `${config.API_URL}/public/security/verification/code`,
		name: "获取验证码",
		get: async function(data={}){
			return await http.get(this.url, data);
		}
	}
	,login: {
		url: `${config.API_URL}/public/security/login`,
		name: "登录",
		post: async function(data={}){
			return await http.post(this.url, data);
		}
	}
	,ver: {
		url: `${config.API_URL}/public/config/ver`,
		name: "获取最新版本号",
		get: async function(params){
			return await http.get(this.url, params);
		}
	},
	uploadImage: {
		url: `${config.API_URL}/public/image/upload`,
		name: "图片上传",
		post: async function(data, config={}){
			return await http.post(this.url, data, config);
		}
	},

	uploadFile: {
		url: `${config.API_URL}/public/file/upload`,
		name: "文件上传",
		post: async function(data, config={}){
			return await http.post(this.url, data, config);
		}
	},

}
