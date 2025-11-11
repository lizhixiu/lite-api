<template>
	<template v-if="type === 'tag'">
		<el-tag :type="getValue() === '1' ? 'success' : 'danger'">{{ getValueLabel() }}</el-tag>
	</template>
</template>
<script>
export default {
	name: 'validColumn',
	data() {
		return {
			booleanMap: {
				'1': '是',
				'0': '否'
			}
		}
	},
	props: {
		type: {type: String, default: 'tag'},
		prop: {type: String, default: ''},
		row: {
			type: Object, default: () => {
			}
		}
	},
	methods: {
		getValueLabel() {
			return this.booleanMap[this.row[this.prop]] || this.row[this.prop]
		},
		getValue() {
			return this.row[this.prop]
		}
	}
}
</script>
