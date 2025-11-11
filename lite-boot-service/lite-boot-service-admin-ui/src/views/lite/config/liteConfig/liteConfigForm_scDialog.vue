<template>
	<sc-dialog $attrs v-model="visible" :title="context.title" @closed="$emit('closed')">
		<el-form :model="form" :rules="rules" ref="dialogForm" label-width="100px" label-position="left">
			<el-card header="基本信息" class="box-card">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="分类" prop="pid">
							<xc-cascader label="上级分类" id="pid" labelField="nameTxt" valueField="id" :dataValue="this.form" fieldMap="pid:id" url="/lite/config/liteConfig/comTreeClass" placeholder="请选择上级分类"/>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="配置类型" prop="typeCode">
							<xc-select label="类型名称" id="typeCode" labelField="paraName" valueField="paraCode" :dataValue="this.form" fieldMap="typeCode:paraCode,typeName:paraName" url="/public/para/comList?classCode=SYS_PARA_TYPE"/>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="配置编码" prop="codeTxt">
							<el-input placeholder="请输入配置编码" v-model="form.codeTxt" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="配置名称" prop="nameTxt">
							<el-input placeholder="请输入配置名称" v-model="form.nameTxt" clearable></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="配置值" prop="valueTxt">
							<el-input placeholder="请输入配置值" v-model="form.valueTxt" clearable></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="是否系统保留" prop="recSysFlag">
							<xc-switch placeholder="请输入是否系统保留" v-model="form.recSysFlag" clearable></xc-switch>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="排序号" prop="recSortNum">
							<el-input-number placeholder="请输入排序号" v-model="form.recSortNum" clearable></el-input-number>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="是否启用" prop="recValidFlag">
							<xc-switch placeholder="请输入是否启用" v-model="form.recValidFlag" clearable></xc-switch>
						</el-form-item>
					</el-col>
				</el-row>
			</el-card>

			<el-card header="备注" style="margin-top: 10px;">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item prop="recRemarksTxt" label-width="0px">
							<el-input v-model="form.recRemarksTxt" placeholder="请输入备注" clearable :rows="4" type="textarea" style="width: 100%;"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-card>

			<el-card header="创建与修改信息" style="margin-top: 10px;">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="创建人" prop="recCreateRef">
							<el-input placeholder="请输入创建人"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="创建时间" prop="recCreateDt">
							<el-input placeholder="请输入创建时间"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="修改人" prop="recUpdateRef">
							<el-input placeholder="请输入修改人"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="修改时间" prop="recUpdateDt">
							<el-input placeholder="请输入修改时间"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

			</el-card>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取 消</el-button>
			<el-button type="primary" @click="this.handleSave">确 定</el-button>
		</template>
	</sc-dialog>
</template>

<script>
import apiConfig from "@/config";
import http from "@/utils/request";
import xcCascader from "@/components/xc/form/xcCascader/index.vue";
import xcSelect from "@/components/xc/form/xcSelect/index.vue";
import xcSwitch from "@/components/xc/form/xcSwitch/index.vue";
import {ElMessage} from "element-plus";

export default {
	name: "liteConfigForm",
	components: {
		xcCascader,
		xcSelect,
		xcSwitch,
	},
	data() {
		return {
			visible: true,
			form: {

			},
			rules: {
				pid: [
					{required: true, message: '请选择上级分类'}
				],
				typeCode: [
					{required: true, message: '请输入配置类型'}
				],
				codeTxt: [
					{required: true, message: '请输入配置编码'}
				],
				nameTxt: [
					{required: true, message: '请输入配置名称'}
				],
				recSortNum: [
					{required: true, message: '请输入排序号', trigger: 'change'}
				]
			},
			sourceData: {}
			,
		}
	},
	props: {
		title: {
			type: String,
			default: ""
		}, context: {
			type: Object,
			default: () => {}
		}
	},
	mounted() {
		console.log(this.context)
		this.fetchData();
	},
	methods: {
		// 加载数据
		fetchData() {
			 if (this.context.dataBind) {
				 http.post(`${apiConfig.API_URL}` + this.context.dataBind, this.context.param).then(res => {
					if (res.code === 200) {
						this.form = res.data;
						// 深拷贝初始数据到 sourceData
						this.sourceData = JSON.parse(JSON.stringify(res.data));
					} else {
						this.$message.error(res.msg);
					}
				});
			}
		},
		// 对比数据，找出有变化的字段
		getChangedData() {
			const changedData = {};
			for (const key in this.form) {
				// eslint-disable-next-line no-prototype-builtins
				if (this.form.hasOwnProperty(key)) {
					const formValue = this.form[key];
					const sourceValue = this.sourceData[key];
					if (JSON.stringify(formValue) !== JSON.stringify(sourceValue)) {
						changedData[key] = formValue;
					}
				}
			}
			return changedData;
		},
		handleSave() {

			let actionType = this.context.action !== '' ? this.context.action : 'add';
			let url = '';
			let successMsg = '';
			// eslint-disable-next-line vue/no-mutating-props
			this.context.insertAction = '/lite/config/liteConfig/insert';
			// eslint-disable-next-line vue/no-mutating-props
			this.context.updateAction = '/lite/config/liteConfig/update';
			// eslint-disable-next-line vue/no-mutating-props
			this.context.insertSuccessMsg = '新增成功';
			// eslint-disable-next-line vue/no-mutating-props
			this.context.updateSuccessMsg = '更新成功';
			// eslint-disable-next-line vue/no-mutating-props
			this.context.copySuccessMsg = '复制成功';
			switch (actionType) {
				case 'add':
					if (this.context.insertSuccessMsg) successMsg = this.context.insertSuccessMsg;
					if (this.context.insertAction) url = this.context.insertAction;
					break;
				case 'copy':
					if (this.context.copySuccessMsg) successMsg = this.context.copySuccessMsg;
					if (this.context.insertAction) url = this.context.insertAction;
					break;
				case 'edit':
					if (this.context.updateSuccessMsg) successMsg = this.context.updateSuccessMsg;
					if (this.context.updateAction) url = this.context.updateAction;
					break;
				case 'post':
					if (this.context.postSuccessMsg) successMsg = this.context.postSuccessMsg;
					if (this.context.postAction) url = this.context.postAction;
					break;
				default:
					break;
			}

			if (url === '') {
				ElMessage.error('未配置接口参数');
				return
			}

			this.$refs.dialogForm.validate(async (valid) => {
				if (!valid) {
					ElMessage.warning('请填写完整信息');
					return;
				}

				url = `${apiConfig.API_URL}` + url;

				let dataToSubmit;
				if (actionType === "add" || actionType === "copy" || actionType === "post") {
					// 新增或拷贝操作提交完整表单数据
					dataToSubmit = this.form;
				}else if (actionType === "edit") {

					dataToSubmit = this.getChangedData();
					// 如果没有变化，提示用户并返回
					if (Object.keys(dataToSubmit).length === 0) {
						this.$message.info('数据未发生变化，无需提交');
						this.isSaveing = false;
						return;
					}

					// 编辑操作提交主键ID
					dataToSubmit.id = this.form.id;

					if(this.context.updateParamsKey){
						this.context.updateParamsKey.forEach((key) => {
							dataToSubmit[key] = this.form[key];
						});
					}
				}

				await http.post(url, dataToSubmit, {
					headers: {
						//'response-status': 401
					}
				}).then((res) => {
					if (res !== null && res.code === 200) {
						ElMessage.success(successMsg || "操作成功")
						//成功后：回调刷新
						if (typeof this.context.afterSuccess === 'function') {
							try {
								const respContext = {
									actionType, res,
								}
								this.context.afterSuccess(respContext);
							} catch (e) {
								console.warn(e);
							}
						}
					} else {
						// console.log("<UNK>", res);
						ElMessage.error(res.message || '操作失败');
						return;
					}
				}).catch(() => {
					// this.$message.error("操作失败");
				});
			})
		},
	}
}
</script>

<style>
</style>
