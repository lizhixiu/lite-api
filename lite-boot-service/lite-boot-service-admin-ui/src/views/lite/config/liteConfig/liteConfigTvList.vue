<template>
	<el-container>
		<el-aside width="300px">
			<xcTree title="系统配置树" label="nameTxt" url="/lite/config/liteConfig/comTree"
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
				<xcTable title="系统配置列表" ref="tableRef" url="/lite/config/liteConfig/page" row-key="id" stripe @selection-change="handleTableSelectionChange">
					<el-table-column type="selection" width="50"></el-table-column>
					<el-table-column type="index" label="序号" width="50"></el-table-column>
					<el-table-column label="配置类型" prop="typeRef" align="center" width="120"></el-table-column>
					<el-table-column label="配置编码" prop="codeTxt" width="150"></el-table-column>
					<el-table-column label="配置名称" prop="nameTxt" width="200"></el-table-column>
					<el-table-column label="配置值" prop="valueTxt" width="150"></el-table-column>
					<el-table-column label="排序号" prop="recSortNum" align="center" width="120"
									 sortable></el-table-column>
					<el-table-column label="是否有效" prop="recValidFlag" align="center" width="80">
						<template #default="scope">
							<boolean-column :row="scope.row" prop="recValidFlag"></boolean-column>
						</template>
					</el-table-column>
					<el-table-column label="创建时间" prop="recCreateDt" align="center" width="150"
									 sortable></el-table-column>
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
	name: "liteConfigTvList",
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
			context.title = '系统配置|新增';
			context.src = 'lite/config/liteConfig/liteConfigForm';
			context.actionType = 'add';
			context.param = {pid: this.$refs.treeRef.getCurrentKey()};
			context.dataBind = "/lite/config/liteConfig/prepare";
			context.afterSuccess = this.saveAfterSuccess;
			this.$EVENTS.lay.handleOpen(this, context);
		},
		handleView(row) {
			let context = {};
			context.title = '系统配置|详情';
			context.src = 'lite/config/liteConfig/liteConfigForm';
			context.actionType = 'view';
			context.param = {
				id: row.id
			};
			context.dataBind = "/lite/config/liteConfig/get";
			context.afterSuccess = this.saveAfterSuccess;
			this.$EVENTS.lay.handleOpen(this, context);
		},
		handleEdit(row) {
			let context = {};
			context.title = '系统配置|编辑';
			context.src = 'lite/config/liteConfig/liteConfigForm';
			context.actionType = 'edit';
			context.param = {
				id: row.id
			}
			context.dataBind = "/lite/config/liteConfig/get";
			context.afterSuccess = this.saveAfterSuccess;
			this.$EVENTS.lay.handleOpen(this, context);
		},
		handleCopy(row) {
			let context = {};
			context.title = '系统配置|拷贝';
			context.src = 'lite/config/liteConfig/liteConfigForm';
			context.actionType = 'copy';
			context.param = {
				rowId: row.id
			}
			context.dataBind = "/lite/config/liteConfig/copy";
			context.afterSuccess = this.saveAfterSuccess;
			this.$EVENTS.lay.handleOpen(this, context);
		},
		handleDelete(row) {
			let context = {};
			context.param = {id: row.id}
			context.deleteAction = '/lite/config/liteConfig/delete';
			context.confirmMsg = `确定删除记录吗？`;
			context.successMsg = '删除成功!';
			context.afterSuccess = this.deleteAfterSuccess;
			this.$EVENTS.form.handleDelete(context);
		},
		handleBatchDelete() {
			let context = {};
			context.param = {ids: this.$refs.tableRef.getSelectedIds()};
			context.deleteAction = '/lite/config/liteConfig/deletes';
			context.confirmMsg = `确定删除选中的 ${this.$refs.tableRef.getSelectedIds().length} 项吗？`;
			context.successMsg = '删除成功!';
			context.afterSuccess = this.deleteAfterSuccess;
			this.$EVENTS.form.handleBatchDelete(context);
		},
		saveAfterSuccess() {
			this.$EVENTS.base.handleRefresh(this.$refs.tableRef);
			this.$EVENTS.base.handleRefresh(this.$refs.treeRef);
			this.$xcModal.close();
		},
		deleteAfterSuccess() {
			this.$EVENTS.base.handleRefresh(this.$refs.tableRef);
			this.$EVENTS.base.handleRefresh(this.$refs.treeRef);
		},
		handleTreeNodeClick(data) {
			this.$refs.tableRef.reload({pid: data.id})
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
