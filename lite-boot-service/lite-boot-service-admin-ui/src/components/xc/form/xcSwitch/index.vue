<template>
	<el-switch
		ref="magicSwitch"
		v-model="selectValue"
		:active-value="activeValue"
		:inactive-value="inactiveValue"
		:active-text="activeText"
		:inactive-text="inactiveText"
		:active-color="activeColor"
		:inactive-color="inactiveColor"
		v-bind="propsProps"
		@change="handleChange"
	/>
</template>

<script>
export default {
	name: 'XcSwitch',
	data() {
		return {
			// 双向绑定的值，对传入值进行类型处理
			selectValue: this.processValue(this.modelValue)
		};
	},
	props: {
		modelValue: {
			type: [Boolean, String, Number],
			default: false
		},
		activeValue: {
			type: [Boolean, String, Number],
			default: '1' // 默认激活值设为字符串 "1"
		},
		inactiveValue: {
			type: [Boolean, String, Number],
			default: '0' // 默认未激活值设为字符串 "0"
		},
		activeText: {
			type: String,
			default: ''
		},
		inactiveText: {
			type: String,
			default: ''
		},
		activeColor: {
			type: String,
			default: ''
		},
		inactiveColor: {
			type: String,
			default: ''
		},
		props: {
			type: Object,
			default: () => ({})
		}
	},
	computed: {
		// 为了避免与 props 关键字冲突，使用 propsProps
		propsProps() {
			return this.props;
		}
	},
	watch: {
		// 监听 modelValue 的变化，更新 selectValue
		modelValue: {
			immediate: true, // 立即执行一次监听函数
			handler(newValue) {
				this.selectValue = this.processValue(newValue);
			}
		}
	},
	methods: {
		handleChange(newValue) {
			this.$emit('update:modelValue', newValue);
			this.$emit('change', newValue);
		},
		// 处理传入值的类型
		processValue(value) {
			if (typeof value === 'string') {
				return value === this.activeValue.toString() ? this.activeValue : this.inactiveValue;
			}
			return value;
		}
	}
};
</script>
