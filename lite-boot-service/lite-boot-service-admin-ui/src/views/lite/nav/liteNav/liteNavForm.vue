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
					<el-form-item label="上级目录" prop="pid">
						<xc-cascader label="上级目录" id="pid" labelField="nameTxt" valueField="id"
									 :dataValue="this.formData" fieldMap="pid:id"
									 url="/lite/nav/liteNav/comTree"
									 placeholder="请选择上级目录"/>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="导航名称" prop="nameTxt">
						<el-input v-model="formData.nameTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="导航编码" prop="codeTxt">
						<el-input v-model="formData.codeTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="导航别名" prop="aliasTxt">
						<el-input v-model="formData.aliasTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="导航图标" prop="iconTxt">
						<sc-icon-select v-model="formData.iconTxt" clearable></sc-icon-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item label="导航url" prop="urlTxt">
						<el-input v-model="formData.urlTxt" clearable></el-input>
					</el-form-item>
				</el-col>

			</el-row>
			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item label="组件" prop="componentTxt">
						<el-input v-model="formData.componentTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="导航类型" prop="typeCode">
<!--						<el-input v-model="formData.typeCode" clearable></el-input>-->
						<xc-select label="导航类型" id="typeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="typeCode:codeTxt,typeRef:nameTxt"
								   url="/public/para/comList?classCode=SYS_NAV_TYPE"/>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="导航链接类型" prop="linkTypeCode">
<!--						<el-input v-model="formData.linkTypeCode" clearable></el-input>-->
						<xc-select label="导航链接类型" id="linkTypeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="linkTypeCode:codeTxt,linkTypeRef:nameTxt"
								   url="/public/para/comList?classCode=SYS_LINK_TYPE"/>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item label="导航提示" prop="tipsTxt">
						<el-input type="textarea" v-model="formData.tipsTxt" rows="4" clearable></el-input>
					</el-form-item>
				</el-col>

			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="打开方式" prop="openModeCode">
<!--						<el-input v-model="formData.openModeCode" clearable></el-input>-->
						<xc-select label="打开方式" id="openModeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="linkTypeCode:codeTxt,linkTypeRef:nameTxt"
								   url="/public/para/comList?classCode=SYS_NAV_OPEN_MODE"/>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否显示" prop="showFlag">
						<xc-switch v-model="formData.showFlag"></xc-switch>
					</el-form-item>
				</el-col>

			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="是否缓存" prop="keepAliveFlag">
						<xc-switch v-model="formData.keepAliveFlag"></xc-switch>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="系统保留" prop="recSysFlag">
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
import scIconSelect from "@/components/scIconSelect/index.vue";

export default {
	name: "liteNavForm",
	components: {
		scIconSelect,
		xcCascader,
		xcSelect,
		xcSwitch,
	},
	data() {
		return {
			formData: {},
			sourceData: {},
			rules: {
			pid: [
				{required: true, message: '请选择上级导航', trigger: 'blur'}],
				nameTxt: [{required: true, message: '请输入导航名称', trigger: 'blur'}],
				typeCode: [{required: true, message: '请输入导航类型', trigger: 'blur'}],
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
			saveConfig.insertAction = '/lite/nav/liteNav/insert';
			saveConfig.updateAction = '/lite/nav/liteNav/update';
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
