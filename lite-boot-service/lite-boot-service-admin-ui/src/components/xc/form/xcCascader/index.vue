<template>
	<el-cascader
		v-model="value"
		:loading="loading"
		:options="treeData"
		:props="treeProps"
		:clearable="clearable"
		@change="handleUpdateValue"
		:show-all-levels="false"
		:checkStrictly="true"
		style="width: 100%;"
	>
		<template #option="{ node }">
			<slot name="option" :data="node"></slot>
		</template>
	</el-cascader>
</template>

<script>
import DEFAULT_CONFIG from "@/config";
import config from "./config/cascaderSelect";
import http from "@/utils/request";

export default {
	props: {
		id: { type: String, default: "" },
		url: { type: String, default: "" },
		params: { type: Object, default: () => ({}) },
		labelField: { type: String, default: config.props.label },
		valueField: { type: String, default: config.props.value },
		childrenField: { type: String, default: 'children' }, // 新增：树形结构子节点字段
		dataValue: { type: Object, default: () => ({}) },
		fieldMap: { type: String },
		clearable: { type: Boolean, default: true },
	},
	data() {
		return {
			value: "",
			loading: false,
			treeData: [], // 树形结构数据
			treeProps: {
				label: this.labelField,
				value: this.valueField,
				checkStrictly: true,
				children: this.childrenField // 配置树形结构字段
			},
			initloading: false
		};
	},
	watch: {
		// 监听dataValue变化，确保在父组件数据加载完成后能正确显示值
		dataValue: {
			handler(newVal) {
				if (newVal && newVal[this.id] !== undefined) {
					this.value = newVal[this.id];
				}
			},
			deep: true
		}
	},
	created() {
		this.initloading = true;
		this.getRemoteData();
	},
	methods: {
		async getRemoteData() {
			this.loading = true;
			try {
				const res = this.url ? await http.post(DEFAULT_CONFIG.API_URL + this.url, this.params) : {};
				const response = config.parseData(res);
				// 确保数据是树形结构，如果不是需要转换
				this.treeData = response.data.list;

				// 在数据加载完成后，确保设置当前值
				this.$nextTick(() => {
					if (this.dataValue && this.dataValue[this.id] !== undefined) {
						this.value = this.dataValue[this.id];
					}
				});
			} catch (error) {
				console.error('Failed to load tree data:', error);
				this.treeData = [];
			} finally {
				this.loading = false;
				this.initloading = false;
			}
		},
		handleUpdateValue(path) {
			if (!path || path.length === 0) return;
			// 取路径最后一个值作为选中值
			const val = path[path.length - 1];
			// 查找选中节点的完整数据
			const selectedNode = this.findNodeByValue(this.treeData, val);
			if (selectedNode && this.fieldMap) {
				this.updateDataValue(selectedNode);
			}
			// 触发v-model更新
			this.$emit('update:modelValue', val);
		},
		// 递归查找选中节点
		findNodeByValue(nodes, value) {
			for (const node of nodes) {
				if (node[this.valueField] === value) {
					return node;
				}
				if (node[this.childrenField] && node[this.childrenField].length) {
					const found = this.findNodeByValue(node[this.childrenField], value);
					if (found) return found;
				}
			}
			return null;
		},
		// 更新dataValue字段映射
		updateDataValue: function (selectedNode) {
			const fieldmapStrList = this.fieldMap.split(',');
			fieldmapStrList.forEach(fieldStr => {
				const [fieldmapKey, fieldmapValue = fieldmapKey] = fieldStr.split(':');
				// eslint-disable-next-line vue/no-mutating-props
				this.dataValue[fieldmapKey] = selectedNode[fieldmapValue];
			});
		}
	}
};
</script>

<style scoped>
.xc-select-loading i {
	font-size: 14px;
}

.dark .el-cascader {
	background: var(--el-bg-color-overlay);
	border-color: var(--el-border-color-light);
}
</style>
