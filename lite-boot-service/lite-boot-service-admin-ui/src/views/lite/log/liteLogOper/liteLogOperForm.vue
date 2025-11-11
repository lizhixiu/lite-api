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
          <el-form-item label="主键" prop="id">
            <el-input v-model="formData.id" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接口名" prop="apiNameTxt">
            <el-input v-model="formData.apiNameTxt" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否删除" prop="recDelFlag">
            <el-input v-model="formData.recDelFlag" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="路径" prop="apiPathTxt">
            <el-input v-model="formData.apiPathTxt" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="方法" prop="apiMethodCode">
            <el-input v-model="formData.apiMethodCode" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="耗时" prop="costTimeNum">
            <el-input v-model="formData.costTimeNum" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户代理" prop="userAgentTxt">
            <el-input v-model="formData.userAgentTxt" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户IP" prop="userIpTxt">
            <el-input v-model="formData.userIpTxt" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人ID" prop="recCreateId">
            <el-input v-model="formData.recCreateId" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="token" prop="loginTokenTxt">
            <el-input v-model="formData.loginTokenTxt" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人" prop="recCreateRef">
            <el-input v-model="formData.recCreateRef" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="recCreateDt">
            <el-input v-model="formData.recCreateDt" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业ID" prop="recOrgId">
            <el-input v-model="formData.recOrgId" clearable></el-input>
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
  name: "liteLogOperForm",
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
      saveConfig.insertAction = '/lite/log/liteLogOper/insert';
      saveConfig.updateAction = '/lite/log/liteLogOper/update';
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
