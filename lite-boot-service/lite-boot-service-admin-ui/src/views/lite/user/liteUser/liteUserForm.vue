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

				<el-col :span="12">
					<el-form-item label="登录账号" prop="loginCodeTxt">
						<el-input v-model="formData.loginCodeTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="用户类型" prop="typeCode">
						<xc-select label="用户类型" id="typeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="typeCode:codeTxt"
								   url="/public/para/comList?classCode=SYS_USER_TYPE"/>
					</el-form-item>
				</el-col>
			</el-row>

			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="姓名" prop="nameTxt">
						<el-input v-model="formData.nameTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="用户编码" prop="codeTxt">
						<el-input v-model="formData.codeTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="昵称" prop="nicknameTxt">
						<el-input v-model="formData.nicknameTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="邮箱" prop="emailTxt">
						<el-input v-model="formData.emailTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="手机号" prop="phoneTxt">
						<el-input v-model="formData.phoneTxt" clearable></el-input>
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="微信" prop="wechatTxt">
						<el-input v-model="formData.wechatTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="性别" prop="genderCode">
						<xc-select label="性别" id="genderCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="genderCode:codeTxt,genderRef:nameTxt"
								   url="/public/para/comList?classCode=SYS_USER_GENDER_TYPE"/>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="出生日期" prop="birthdayDt">
						<el-date-picker v-model="formData.birthdayDt" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%"></el-date-picker>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="身份证" prop="cnidTxt">
						<el-input v-model="formData.cnidTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="QQ" prop="qqTxt">
						<el-input v-model="formData.qqTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="头像URL" prop="avatarUrlTxt">
						<el-input v-model="formData.avatarUrlTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="名称首字母" prop="firstLettersTxt">
						<el-input v-model="formData.firstLettersTxt" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="名称全拼" prop="fullLettersTxt">
						<el-input v-model="formData.fullLettersTxt" clearable></el-input>
					</el-form-item>
				</el-col>
			</el-row>


			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="是否隐藏" prop="hideFlag">
						<xc-switch v-model="formData.hideFlag"></xc-switch>
					</el-form-item>
				</el-col>
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

		<el-card header="登录控制信息" style="margin-top: 10px;">
			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="最后登录时间" prop="loginLastDt">
						<el-input v-model="formData.loginLastDt" disabled></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="最后登录IP" prop="loginLastIp">
						<el-input v-model="formData.loginLastIp" disabled></el-input>
					</el-form-item>
				</el-col>
			</el-row>


			<el-row :gutter="20">

				<el-col :span="12">
					<el-form-item label="密码修改标识" prop="updatePwdFlag">
						<xc-switch v-model="formData.updatePwdFlag"></xc-switch>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="注销时间" prop="invalidDt">
						<el-date-picker v-model="formData.invalidDt" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期时间" style="width: 100%"></el-date-picker>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-form-item label="生效类型" prop="EFFECTTypeCode">
						<xc-select label="生效类型" id="effectTypeCode" labelField="nameTxt" valueField="codeTxt"
								   :dataValue="this.formData" fieldMap="effectTypeCode:codeTxt"
								   url="/public/para/comList?classCode=SYS_USER_EFFECT_TYPE"/>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="生效时间" prop="effectDt">
						<el-date-picker v-model="formData.effectDt" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期时间" style="width: 100%"></el-date-picker>
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
	name: "liteUserForm",
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
				loginCodeTxt: [{required: true, message: "请输入登录账号", trigger: "blur"}],
				nameTxt: [{required: true, message: "请输入姓名", trigger: "blur"}],
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
			saveConfig.insertAction = '/lite/user/liteUser/insert';
			saveConfig.updateAction = '/lite/user/liteUser/update';
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
