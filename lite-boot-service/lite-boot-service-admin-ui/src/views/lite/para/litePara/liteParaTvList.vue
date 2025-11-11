<template>
  <el-container>
    <el-aside width="300px">
      <xcTree title="参数树" label="nameTxt" url="/lite/para/liteParaClass/tree"
              :onTreeNodeClick="handleTreeNodeClick"
              :defaultExpandAll="true"
              :showCheckbox="false"
              ref="treeRef">
      </xcTree>
    </el-aside>
    <el-container>
      <el-header>
        <div class="left-panel">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="danger" plain icon="el-icon-delete" @click="handleBatchDelete" :disabled="tableSelection.length===0">删除</el-button>
        </div>
        <div class="right-panel">
          <div class="right-panel-search">
            <el-input v-model="search.keywordTxt" placeholder="关键词" clearable></el-input>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch"></el-button>
          </div>
        </div>
      </el-header>
      <el-main class="nopadding">
        <xcTable title="参数表列表" ref="tableRef" url="/lite/para/litePara/page" row-key="id" stripe @selection-change="handleTableSelectionChange">
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="50"></el-table-column>
          <el-table-column label="分类编码" prop="classCode" width="180" ></el-table-column>
          <el-table-column label="参数编码" prop="codeTxt" width="180" ></el-table-column>
          <el-table-column label="参数名称" prop="nameTxt" width="180" ></el-table-column>
          <el-table-column label="参数值" prop="valueTxt" width="180" ></el-table-column>
          <el-table-column label="排序号" prop="recSortNum" width="180" ></el-table-column>
          <el-table-column label="是否有效" prop="recValidFlag" align="center" width="80">
            <template #default="scope">
              <boolean-column :row="scope.row" prop="recValidFlag"></boolean-column>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="recCreateDt" width="180" ></el-table-column>
          <el-table-column label="操作" fixed="right" align="center" width="240">
            <template #default="scope">
              <el-button-group>
                <el-button text type="primary" size="small" @click="handleView(scope.row)">查看
                </el-button>
                <el-button text type="primary" size="small" @click="handleEdit(scope.row)">编辑
                </el-button>
                <el-button text type="primary" size="small" @click="handleCopy(scope.row)">复制
                </el-button>
                <el-button text type="danger" size="small" @click="handleDelete(scope.row)">删除
                </el-button>
              </el-button-group>
            </template>
          </el-table-column>
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
  name: "liteParaTvList",
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
      context.title = '参数表|新增';
      context.src = 'lite/para/litePara/liteParaForm';
      context.actionType = 'add';
      context.param = {classId: this.$refs.treeRef.getCurrentKey()};
      context.dataBind = "/lite/para/litePara/prepare";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleView(row) {
      let context = {};
      context.title = '参数表|详情';
      context.src = 'lite/para/litePara/liteParaForm';
      context.actionType = 'view';
      context.param = {
        id: row.id
      };
      context.dataBind = "/lite/para/litePara/get";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleEdit(row) {
      let context = {};
      context.title = '参数表|编辑';
      context.src = 'lite/para/litePara/liteParaForm';
      context.actionType = 'edit';
      context.param = {
        id: row.id
      }
      context.dataBind = "/lite/para/litePara/get";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleCopy(row) {
      let context = {};
      context.title = '参数表|拷贝';
      context.src = 'lite/para/litePara/liteParaForm';
      context.actionType = 'copy';
      context.param = {
        rowId: row.id
      }
      context.dataBind = "/lite/para/litePara/copy";
      context.afterSuccess = this.saveAfterSuccess;
      this.$EVENTS.lay.handleOpen(this, context);
    },
    handleDelete(row) {
      let context = {};
      context.param = {id: row.id}
      context.deleteAction = '/lite/para/litePara/delete';
      context.confirmMsg = `确定删除记录吗？`;
      context.successMsg = '删除成功!';
      context.afterSuccess = this.deleteAfterSuccess;
      this.$EVENTS.form.handleDelete(context);
    },
    handleBatchDelete() {
      let context = {};
      context.param = {ids: this.$refs.tableRef.getSelectedIds()};
      context.deleteAction = '/lite/para/litePara/deletes';
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
      this.$refs.tableRef.reload({classId: data.id})
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
