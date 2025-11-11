<template>
	<el-container>
		<el-card :header="title" :bordered="true" style="height: 100%;width: 100%;">
			<el-header>
				<el-input v-model="treeFilterText" placeholder="输入关键字进行过滤" clearable></el-input>
			</el-header>
			<el-main class="nopadding">
				<el-tree ref="treeRef"
						 class="xc-tree"
						 :data="treeList"
						 :node-key="id"
						 :props="treeProps"
						 :show-checkbox="showCheckbox"
						 :highlight-current="true"
						 :expand-on-click-node="false"
						 :filter-node-method="treeFilterNode"
						 @node-click="onTreeNodeClick"
						 @check="onTreeCheck"
						 :default-expand-all="defaultExpandAll"
				>
					<template #default="{ node, data }">
						<div class="tree-node-content">
							<!-- 动态图标 -->
							<el-icon v-if="this.showIcon" :size="16" class="tree-node-icon">
								<component v-if="this.showIcon" :is="this.onRenderIcon(data,node)"/>
							</el-icon>
							<!-- 带颜色的文本 -->
							<span :style="{ color: data.color || '#333' }" class="tree-node-label">{{
									node.label
								}}</span>
						</div>
					</template>
				</el-tree>
			</el-main>
		</el-card>
	</el-container>
</template>
<script>
import apiConfig from "@/config";
import http from "@/utils/request";

export default {
	name: 'xcTree',
	components: {},
	data() {
		return {
			treeFilterText: '',
			treeList: [],
			treeProps: {},
			isExpanded: false,
			isAllSelected: false,
		}
	},
	props: {
		id: {
			type: String,
			default: 'id'
		},
		title: {
			type: String,
			default: ''
		},
		url: {
			type: String,
			default: ''
		},
		label: {
			type: String,
			default: 'nameTxt'
		},
		defaultExpandAll: {
			type: Boolean,
			default: false
		},
		showCheckbox: {
			type: Boolean,
			default: false
		},
		onTreeNodeClick: {
			type: Function,
			default: () => {
			}
		},
		onTreeCheck: {
			type: Function,
			default: () => {
			}
		},
		showIcon: {
			type: Boolean,
			default: true
		},
		renderIcon: {
			type: Function,
			default: null
		}
	},
	mounted() {
		this.treeProps.label = this.label;
		this.isExpanded = this.defaultExpandAll;
		this.loadTreeData()
	},
	watch: {
		treeFilterText(val) {
			this.$refs.treeRef.filter(val);
		}
	},
	methods: {
		loadTreeData() {
			const apiUrl = `${apiConfig.API_URL}` + this.url;
			http.post(apiUrl).then(res => {
				if (res.code === 200) {
					if (res.data.list) {
						this.treeList = res.data.list;
					} else {
						this.treeList = res.data;
					}
				}
			})
		},
		//树过滤
		treeFilterNode(value, data) {
			if (!value) return true;
			var targetText = data[this.label];
			return targetText.indexOf(value) !== -1;
		},

		getCurrentKey() {
			return this.$refs.treeRef.getCurrentKey();
		},
		getCurrentNode() {
			return this.$refs.treeRef.getCurrentNode();
		},
		refresh() {
			this.loadTreeData();
		},
		onRenderIcon(data, node) {
			// 修复了 renderIcon 的使用方式
			if (!this.renderIcon) {
				// 根据节点是否展开来显示不同的文件夹图标
				if (!node.isLeaf) {
					// 非叶子节点（文件夹）
					if (node.expanded) {
						// 展开状态显示打开的文件夹图标
						return 'el-icon-folder-opened';
					} else {
						// 未展开状态显示关闭的文件夹图标
						return 'el-icon-folder';
					}
				} else {
					// 叶子节点显示文件图标
					return 'el-icon-document';
				}
			} else {
				return this.renderIcon(data, node);
			}
		}
	}
}
</script>
<style>

.tree-node-content {
	display: flex;
	align-items: center;
	gap: 6px;
}
.tree-node-icon {
	vertical-align: middle;
}
.tree-node-label {
	vertical-align: middle;
}
</style>
