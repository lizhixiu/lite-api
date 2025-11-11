<template>
  <el-container>
    <el-aside width="300px">
		<xcTree title="部门树" label="name" url="/lite/dept/liteDept/comTreeOrgDept"
				:onTreeNodeClick="handleTreeNodeClick"
				:defaultExpandAll="true"
				:showCheckbox="false"
				ref="treeRef">
		</xcTree>
    </el-aside>
    <el-container>
      <el-header>
        <div class="left-panel">
		</div>
        <div class="right-panel">
          <div class="right-panel-search">
            <el-input v-model="search.keywordTxt" placeholder="关键词" clearable></el-input>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch"></el-button>
          </div>
        </div>
      </el-header>
      <el-main class="nopadding">
        <xcTable title="登录日志表列表" ref="tableRef" url="/lite/log/liteLogLogin/page" row-key="id" stripe @selection-change="handleTableSelectionChange">
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="50"></el-table-column>
          <el-table-column label="登录账号" prop="loginCodeTxt" width="180" ></el-table-column>
          <el-table-column label="失败密码" prop="failPwdTxt" width="180" ></el-table-column>
          <el-table-column label="失败信息" prop="failTxt" width="180" ></el-table-column>
<!--          <el-table-column label="登录类型（成功、失败）" prop="loginTypeCode" width="180" ></el-table-column>-->
          <el-table-column label="浏览器" prop="loginBrowserTxt" width="180" ></el-table-column>
          <el-table-column label="操作系统" prop="loginOsTxt" width="180" ></el-table-column>
          <el-table-column label="地理位置" prop="loginAddressTxt" width="180" ></el-table-column>
          <el-table-column label="ip地址" prop="loginIpTxt" width="180" ></el-table-column>
          <el-table-column label="token" prop="loginTokenTxt" width="180" ></el-table-column>
			<el-table-column label="创建时间" prop="recCreateDt" width="180" ></el-table-column>
        </xcTable>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import xcTree from "@/components/xc/data/xcTree"
import xcTable from "@/components/xc/data/xcTable"
import booleanColumn from "@/components/xc/data/xcTable/booleanColumn"

export default {
  name: "liteLogLoginTvList",
  components: {
    xcTree
    , xcTable
    , booleanColumn
  },
  data() {
    return {
      tableSelection: []
      , search: {
        keywordTxt: ""
      }
    }
  },
  watch: {},
  mounted() {
  },
  methods: {
    handleAdd() {
      let context = {};
      context.title = '登录日志表|新增';
      context.src = 'lite/log/liteLogLogin/liteLogLoginForm';
      context.actionType = 'add';
      context.param = {recOrgId: this.$refs.treeRef.getCurrentKey()};
      context.dataBind = "/lite/log/liteLogLogin/prepare";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleView(row) {
      let context = {};
      context.title = '登录日志表|详情';
      context.src = 'lite/log/liteLogLogin/liteLogLoginForm';
      context.actionType = 'view';
      context.param = {
        id: row.id
      };
      context.dataBind = "/lite/log/liteLogLogin/get";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleEdit(row) {
      let context = {};
      context.title = '登录日志表|编辑';
      context.src = 'lite/log/liteLogLogin/liteLogLoginForm';
      context.actionType = 'edit';
      context.param = {
        id: row.id
      }
      context.dataBind = "/lite/log/liteLogLogin/get";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleCopy(row) {
      let context = {};
      context.title = '登录日志表|拷贝';
      context.src = 'lite/log/liteLogLogin/liteLogLoginForm';
      context.actionType = 'copy';
      context.param = {
        rowId: row.id
      }
      context.dataBind = "/lite/log/liteLogLogin/copy";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleDelete(row) {
      let context = {};
      context.param = {id: row.id}
      context.deleteAction = '/lite/log/liteLogLogin/delete';
      context.confirmMsg = `确定删除记录吗？`;
      context.successMsg = '删除成功!';
      context.afterSuccess = this.deleteAfterSuccess;
      this.$EVENTS.form.handleDelete(context);
    },
    handleBatchDelete() {
      let context = {};
      context.param = {ids: this.$refs.tableRef.getSelectedIds()};
      context.deleteAction = '/lite/log/liteLogLogin/deletes';
      context.confirmMsg = `确定删除选中的 ${this.$refs.tableRef.getSelectedIds().length} 项吗？`;
      context.successMsg = '删除成功!';
      context.afterSuccess = this.deleteAfterSuccess;
      this.$EVENTS.form.handleBatchDelete(context);
    },
    saveAfterSuccess() {
      this.$EVENTS.base.handleRefresh(this.$refs.tableRef);
      this.$xcModal.close();
    },
    deleteAfterSuccess() {
      this.$EVENTS.base.handleRefresh(this.$refs.tableRef);
    },
    handleTreeNodeClick(data) {
		if(data.type==='ORG'){
			this.$refs.tableRef.reload({orgId: data.id})
		}else if(data.type==='DEPT'){
			this.$refs.tableRef.reload({deptId: data.id})
		}
    },
    handleTableSelectionChange(selection) {
      this.tableSelection = selection
    },
    handleSearch() {
      this.$refs.tableRef.upData(this.search)
    }
  }
}
</script>
