<template>
	<el-card shadow="hover" header="版本信息">
		<div style="height: 210px;text-align: center;">
			<img src="img/ver.svg" style="height:140px"/>
			<h2 style="margin-top: 15px;">lite-boot {{$CONFIG.CORE_VER}}</h2>
			<p style="margin-top: 5px;">最新版本 {{ver}}</p>
		</div>
		<div style="margin-top: 20px;">
			<el-button type="primary" plain round @click="golog">更新日志</el-button>
			<el-button type="primary" plain round @click="gogit">gitee</el-button>
		</div>
	</el-card>
</template>

<script>
	export default {
		title: "版本信息",
		icon: "el-icon-monitor",
		description: "当前项目版本信息",
		data() {
			return {
				ver: 'loading...'
			}
		},
		mounted() {
			this.getVer()
		},
		methods: {
			async getVer(){
				const ver = await this.$API.public.ver.get()
				this.ver = ver.data
				// TODO 临时在进行重新获取菜单权限
				var menu = await this.$API.system.menu.myMenus.get()
				if(menu.code == 200){
					if(menu.data.menu.length==0){
						this.islogin = false
						this.$alert("当前用户无任何菜单权限，请联系系统管理员", "无权限访问", {
							type: 'error',
							center: true
						})
						return false
					}
					this.$TOOL.data.set("MENU", menu.data.menu)
					this.$TOOL.data.set("PERMISSIONS", menu.data.permissions)
					this.$TOOL.data.set("DASHBOARDGRID", menu.data.dashboardGrid)
				}else{
					this.islogin = false
					this.$message.warning(menu.message)
					return false
				}
			},
			golog(){
				window.open("https://gitee.com/xc_java/lite-api")
			},
			gogit(){
				window.open("https://gitee.com/xc_java/lite-api")
			}
		}
	}
</script>
