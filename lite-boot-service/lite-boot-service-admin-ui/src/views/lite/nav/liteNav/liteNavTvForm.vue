<template>
	<el-container>
		<el-aside width="300px">
			<xcTree title="导航树" label="nameTxt" url="/lite/nav/liteNav/comTree"
					:onTreeNodeClick="handleTreeNodeClick"
					:defaultExpandAll="true"
					:showCheckbox="false"
					ref="treeRef">
			</xcTree>
		</el-aside>
		<el-container>
			<el-main class="nopadding" style="padding:20px;" ref="main">
				<liteNavForm ref="formRef"></liteNavForm>
			</el-main>
		</el-container>
	</el-container>
</template>

<script>

import liteNavForm from './liteNavForm'
import xcTree from "@/components/xc/data/xcTree"

export default {
	name: "liteNavTvForm",
	components: {
		liteNavForm
		, xcTree
	},
	data() {
		return {}
	},
	watch: {},
	mounted() {

	},
	methods: {
		//树点击
		handleTreeNodeClick(data) {
			this.$refs.formRef.setFormData(data)
		},
	}
}
</script>
