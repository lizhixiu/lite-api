<template>

	<el-select v-model="value" :loading="loading" @visible-change="visibleChange"  @change="handleUpdateValue"
			   :clearable="clearable" style="width: 100%;">
		<el-option v-for="item in options" :key="item[props.valueField]" :label="item[props.labelField]"
				   :value="item[props.valueField]">
			<slot name="option" :data="item"></slot>
		</el-option>
	</el-select>

</template>

<script>

import DEFAULT_CONFIG from "@/config";
import config from "./config/select";
import http from "@/utils/request";

export default {
	props: {
		id: {type: String, default: ""},
		url: {type: String, default: ""},
		params: {type: Object, default: () => ({})},
		labelField: {type: String, default: config.props.label},
		valueField: {type: String, default: config.props.value},
		dataValue: {
			type: Object,
			default: () => {
			}
		},
		fieldMap: {
			type: String,
		},
		clearable: {type: Boolean, default: true},
	},
	data() {
		return {
			loading: false,
			options: [],
			props: {
				labelField: this.labelField,
				valueField: this.valueField
			},
			value: "",
			initloading: false
		}
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
		// console.log('select', this.dataValue);
		// console.log('fieldMap', this.fieldMap);
		this.initloading = true
		this.getRemoteData()
	},
	methods: {
		//选项显示隐藏事件
		visibleChange(ispoen) {

			if (ispoen && (this.options ===null ||this.options ===undefined || this.options.length === 0 ) && (this.url || this.dic || this.apiObj)) {
				this.getRemoteData()
			}
		},
		//获取数据
		async getRemoteData() {
			this.loading = true
			var res = {}
			if (this.url) {
				res = await http.post(DEFAULT_CONFIG.API_URL + this.url, this.params)
			}
			var response = config.parseData(res)
			this.options = response.data
			this.loading = false
			this.initloading = false
			// 在数据加载完成后，确保设置当前值
			this.$nextTick(() => {
				if (this.dataValue && this.dataValue[this.id] !== undefined) {
					this.value = this.dataValue[this.id];
				}
			});
		},
		//值改变事件
		handleUpdateValue(val) {
			// eslint-disable-next-line vue/no-mutating-props
			this.value = val
			// 1. 监测值变化
			console.log('原始值:', val);
			// console.log(this.options);
			// console.log("this.fieldMap",this.fieldMap);

			for (let i = 0; i < this.options.length; i++) {
				let selectData = this.options[i];
				if(selectData[this.valueField]=== val ){
					let fieldmapStrList = this.fieldMap.split(",");
					console.log(selectData);
					for(let i = 0;i<fieldmapStrList.length;i++){
						let fieldStrList  = fieldmapStrList[i].split(":");
						let fieldmapKey ;
						let fieldmapValue ;
						// console.log(fieldStrList);
						if(fieldStrList.length==1) {
							fieldmapKey = fieldStrList[0];
							fieldmapValue = fieldStrList[0];
						}else if(fieldStrList.length>1){
							fieldmapKey = fieldStrList[0];
							fieldmapValue = fieldStrList[1];
						}
						// console.log(fieldmapKey);
						// console.log(fieldmapValue);
						// console.log( props.dataValue);
						// console.log( props.dataValue[fieldmapKey]);
						// console.log( selectData[fieldmapValue]);
						// eslint-disable-next-line vue/no-mutating-props
						this.dataValue[fieldmapKey] = selectData[fieldmapValue];
						console.log( this.dataValue );
					}
				}
			}
		}
	}
}
</script>

<style scoped>
.xc-select-loading i {
	font-size: 14px;
}

.dark .el-select {
	background: var(--el-bg-color-overlay);
	border-color: var(--el-border-color-light);
}
</style>
