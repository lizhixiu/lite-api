<template>
	<el-header>
		<div class="right-panel">
		</div>
		<div class="right-panel">
			<el-button type="primary" plain icon="el-icon-check" @click="handleSave">保存</el-button>
			<el-button plain icon="el-icon-close" @click="handleBack">关闭</el-button>
		</div>
	</el-header>
	<el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" label-position="left">
		<el-card header="基本信息" class="box-card">
			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item label="上级部门" prop="rowId">
						<xc-cascader label="上级部门" id="rowId" labelField="name" valueField="id"
									 :dataValue="this.formData" fieldMap="rowId:id"
									 url="/lite/dept/liteDept/comTreeOrgDept"
									 placeholder="请选择上级部门"/>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="岗位名称" prop="nameTxt">
						<el-input v-model="formData.nameTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="岗位简称" prop="shortNameTxt">
						<el-input v-model="formData.shortNameTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="岗位编码" prop="codeTxt">
						<el-input v-model="formData.codeTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="岗位类型" prop="typeCode">
						<xc-select label="岗位类型" id="typeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="typeCode:codeTxt,typeRef:nameTxt"
								   url="/public/para/comList?classCode=SYS_POST_TYPE"/>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="是否系统保留" prop="recSysFlag">
						<xc-switch v-model="formData.recSysFlag"></xc-switch>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="排序号" prop="recSortNum">
						<el-input v-model="formData.recSortNum" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否有效" prop="recValidFlag">
						<xc-switch v-model="formData.recValidFlag"></xc-switch>
					</el-form-item>
				</el-col>
			</el-row>

		</el-card>

		<el-card header="备注" style="margin-top: 10px;">
			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item prop="recRemarksTxt" label-width="0px">
						<el-input v-model="formData.recRemarksTxt" placeholder="请输入备注" clearable :rows="4"
								  type="textarea" style="width: 100%;"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-card>

		<el-card header="创建与修改信息" style="margin-top: 10px;">
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="创建人" prop="recCreateRef">
						<el-input placeholder="请输入创建人" v-model="formData.recCreateRef" disabled></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="创建时间" prop="recCreateDt">
						<el-input placeholder="请输入创建时间" v-model="formData.recCreateDt" disabled></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="修改人" prop="recUpdateRef">
						<el-input placeholder="请输入修改人" v-model="formData.recUpdateRef" disabled></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="修改时间" prop="recUpdateDt">
						<el-input placeholder="请输入修改时间" v-model="formData.recUpdateDt" disabled></el-input>
					</el-form-item>
				</el-col>
			</el-row>

		</el-card>
	</el-form>

</template>

<script>
import xcCascader from "@/components/xc/form/xcCascader/index.vue";
import xcSelect from "@/components/xc/form/xcSelect/index.vue";
import xcSwitch from "@/components/xc/form/xcSwitch/index.vue";

export default {
	name: "litePostForm",
	components: {
		xcCascader,
		xcSelect,
		xcSwitch,
	},
	data() {
		return {
			formData: {},
			sourceData: {},
			rules: {
				rowId: [{ required: true, message: "请选择上级部门", trigger: "blur" }],
				nameTxt: [{ required: true, message: "请输入岗位名称", trigger: "blur" }],
				codeTxt: [{ required: true, message: "请输入岗位编码", trigger: "blur" }],
				typeCode: [{ required: true, message: "请选择岗位类型", trigger: "blur" }],
			}
			, context: {}
		}
	},
	mounted() {
		this.context = {...this.$attrs};
		this.handleFetchData();
	},
	methods: {
		// 加载数据
		handleFetchData() {
			this.$EVENTS.form.handleFetchData(this, this.context);
		},
		setFormData(form, sourceData) {
			this.formData = form;
			this.sourceData = sourceData;
		},
		getFormRef() {
			return this.$refs.formRef;
		},
		getSourceData() {
			return this.sourceData;
		},
		getFormData() {
			return this.formData;
		},
		handleSave() {
			let saveConfig = {};
			saveConfig.insertAction = '/lite/post/litePost/insert';
			saveConfig.updateAction = '/lite/post/litePost/update';
			saveConfig.insertSuccessMsg = '新增成功';
			saveConfig.updateSuccessMsg = '更新成功';
			saveConfig.copySuccessMsg = '复制成功';
			this.context.saveConfig = saveConfig;
			this.$EVENTS.form.handleSave(this, this.context);
		},
		handleBack() {
			this.$xcModal.close();
		}
	},
}
</script>

<style>
</style>
