<template>
	<div>
		<!-- Dialog 模式 -->
		<el-dialog
			v-model="dialogVisible"
			:width="width"
			:before-close="handleClose"
			:show-close="false"
			:modal="shade"
			:modal-append-to-body="true"
			:append-to-body="true"
			:lock-scroll="true"
			:fullscreen="isFullscreen"
			:destroy-on-close="true"
			:z-index="zIndex"
			v-if="mode === 'dialog'"
		>
			<template #header>
				<slot name="header">
					<span class="el-dialog__title">{{ title }}</span>
				</slot>
				<div class="sc-dialog__headerbtn">
					<button v-if="showFullscreen" aria-label="fullscreen" type="button" @click="setFullscreen">
						<el-icon v-if="isFullscreen" class="el-dialog__close"><el-icon-bottom-left /></el-icon>
						<el-icon v-else class="el-dialog__close"><el-icon-full-screen /></el-icon>
					</button>
					<button v-if="showClose" aria-label="close" type="button" @click="closeDialog">
						<el-icon class="el-dialog__close"><el-icon-close /></el-icon>
					</button>
				</div>
			</template>
			<div :style="getContentStyle()">
				<component v-if="Resolved" :is="Resolved" v-bind="contentProps" />
				<slot v-else/>
			</div>
			<template #footer>
				<div v-if="showFooter">
					<el-button v-for="(btn, index) in buttons" :key="index" :type="btn.type" @click="btn.callback">
						{{ btn.text }}
					</el-button>
				</div>
			</template>
		</el-dialog>

		<!-- Drawer 模式 -->
		<el-drawer
			v-model="drawerVisible"
			:title="title"
			:width="width"
			:direction="drawerDirection"
			:before-close="handleClose"
			:z-index="zIndex"
			@opened="$emit('opened')"
			@closed="$emit('closed')"
			v-if="mode === 'drawer'"
		>
			<div style="padding: 20px">
				<component v-if="Resolved" :is="Resolved" v-bind="contentProps" />
				<slot v-else />
			</div>
			<template #footer>
				<div v-if="showFooter">
					<el-button v-for="(btn, index) in buttons" :key="index" :type="btn.type" @click="btn.callback">
						{{ btn.text }}
					</el-button>
				</div>
			</template>
		</el-drawer>
	</div>
</template>

<script>
import { defineAsyncComponent, markRaw } from 'vue'
const viewContext = require.context('@/views', true, /\.vue$/)
const compContext = require.context('@/components', true, /\.vue$/)

export default {
	name: 'xcModal',
	props: {
		modelValue: {
			type: Boolean,
			default: false
		},
		title: {
			type: String,
			default: ''
		},
		width: {
			type: String,
			default: '70%'
		},
		mode: {
			type: String,
			default: 'dialog',
			validator: (value) => ['dialog', 'drawer'].includes(value)
		},
		drawerDirection: {
			type: String,
			default: 'rtl',
			validator: (value) => ['ltr', 'rtl', 'ttb', 'btt'].includes(value)
		},
		shade: {
			type: Boolean,
			default: true
		},
		showClose: {
			type: Boolean,
			default: true
		},
		showFullscreen: { type: Boolean, default: true },
		showFooter: {
			type: Boolean,
			default: false
		},
		buttons: {
			type: Array,
			default: function() {
				return [
					{
						text: "确定",
						type: "primary",
						callback: null
					},
					{
						text: "取消",
						type: "default",
						callback: null
					}
				];
			}
		},
		// 新增：通过字符串路径动态加载内容组件
		src: {
			type: String,
			default: ''
		},
		// z-index，用于多层 Modal
		zIndex: {
			type: Number,
			default: 2000
		}
	},
	data() {
		return {
			dialogVisible: false,
			drawerVisible: false,
			confirmLoading: false,
			// 异步解析得到的组件
			Resolved: null,
			// ... 其他数据 ...
			contentProps: {
				// 初始化要传递的参数
				dataBind:'',
				param: {}
			},
			isFullscreen: false
		};
	},
	watch: {
		title() {
			// 可根据模式更新对应标题，此处示例省略
		},
		// 监听 src 变化，重新解析
		src() {
			this.resolveSrc();
		},
		modelValue: {
			handler(newVal){
				if (this.mode === 'dialog') {
					this.dialogVisible = newVal
				} else {
					this.drawerVisible = newVal
				}
				if(newVal){
					this.isFullscreen = false
				}
			},
			immediate: true
		},
		dialogVisible(newVal) {
			this.$emit('update:modelValue', newVal)
		},
		drawerVisible(newVal) {
			this.$emit('update:modelValue', newVal)
		}
	},
	methods: {
		setContentProps(props) {
			this.contentProps = { ...this.contentProps, ...props };
			return this;
		},
		show() {
			try {
				// 如果传入了 title 参数，通过 emit 通知父组件更新（但 title 现在是 prop，所以这里保持兼容性）
				// 注意：在全局模式下，title 由 Vuex 管理，不需要在这里设置
				if (this.mode === 'dialog') {
					this.dialogVisible = true;
				} else {
					this.drawerVisible = true;
				}
				// Emit opened event for parent handling
				this.$nextTick(() => {
					this.$emit('opened');
				});
			} catch (error) {
				console.error('Error in show method:', error);
			}
		},
		hide() {
			try {
				if (this.mode === 'dialog') {
					this.dialogVisible = false;
				} else {
					this.drawerVisible = false;
				}
				// Emit v-model update
				this.$emit('update:modelValue', false);
				// Always emit close event for parent components
				this.$emit('close');
				// Also emit closed event for additional handling
				this.$nextTick(() => {
					this.$emit('closed');
				});
			} catch (error) {
				console.error('Error in hide method:', error);
				// Force close without events as fallback
				this.dialogVisible = false;
				this.drawerVisible = false;
				this.$emit('update:modelValue', false);
			}
		},
		loading() {
			this.confirmLoading = true;
		},
		hideLoading() {
			this.confirmLoading = false;
		},
		confirm() {
			this.$emit('confirm', {
				loading: this.loading,
				hideLoading: this.hideLoading,
				hide: this.hide,
				title: this.title
			});
		},
		handleClose(done) {
			try {
				this.hide();
				if (typeof done === 'function') {
					done();
				}
			} catch (error) {
				console.error('Error in handleClose:', error);
				// Fallback: force close
				this.dialogVisible = false;
				this.drawerVisible = false;
				if (typeof done === 'function') {
					done();
				}
			}
		},
		//关闭弹窗
		closeDialog(){
			try {
				// Use the standard hide method to ensure proper cleanup and event emission
				this.hide();
			} catch (error) {
				console.error('Error in closeDialog:', error);
				// Fallback: force close
				if (this.mode === 'dialog') {
					this.dialogVisible = false;
				} else {
					this.drawerVisible = false;
				}
				// Still emit the close event for parent components
				this.$emit('close');
			}
		},
		//最大化
		setFullscreen(isFullscreen){
			if(isFullscreen!==null){
				this.isFullscreen = isFullscreen;
				return;
			}
			this.isFullscreen = !this.isFullscreen
		},
		// Initialize button callbacks
		initButtonCallbacks() {
			this.buttons.forEach(btn => {
				if (btn.text === "确定") {
					btn.callback = this.confirm;
				} else if (btn.text === "取消") {
					btn.callback = this.hide;
				}
			});
		},
		// 新增：根据 src 解析要渲染的组件
		resolveSrc() {
			if (!this.src) {
				this.Resolved = null;
				return;
			}
			const keyInViews = './' + this.src + '.vue'
			const keyInComps = this.src.startsWith('components/')
				? './' + this.src.replace(/^components\//, '') + '.vue'
				: null

			const loader = () => new Promise((resolve, reject) => {
				try {
					const mod = viewContext(keyInViews)
					resolve(mod.default || mod)
				} catch (e1) {
					try {
						if (keyInComps) {
							const mod2 = compContext(keyInComps)
							resolve(mod2.default || mod2)
							return
						}
						// 回退：尝试在 components 下直接同名路径（不带前缀）
						const fallbackKey = './' + this.src + '.vue'
						const mod3 = compContext(fallbackKey)
						resolve(mod3.default || mod3)
					} catch (e2) {
						reject(new Error('Component not found: ' + this.src))
					}
				}
			})

			this.Resolved = markRaw(defineAsyncComponent(loader))
		},
		getContentStyle() {
			return {
				padding: '20px',
				maxHeight: this.isFullscreen ? '100%' : '60vh',
				overflowY: 'auto'
			};
		},
	},
	mounted() {
		// Note: Removed direct $refs assignment as it can cause readonly errors
		// Parent components should use ref="modalName" in template instead

		// Call the initialization function
		this.initButtonCallbacks();
		// 初始化解析一次 src
		this.resolveSrc();
	}
};
</script>

<style scoped>
/* 可根据需要添加样式 */
.sc-dialog__headerbtn {position: absolute;top: var(--el-dialog-padding-primary);right: var(--el-dialog-padding-primary);}
.sc-dialog__headerbtn button {padding: 0;background: transparent;border: none;outline: none;cursor: pointer;font-size: var(--el-message-close-size,16px);margin-left: 15px;color: var(--el-color-info);}
.sc-dialog__headerbtn button:hover .el-dialog__close {color: var(--el-color-primary);}
.sc-dialog:deep(.el-dialog).is-fullscreen {display: flex;flex-direction: column;top:0px !important;left:0px !important;}
.sc-dialog:deep(.el-dialog).is-fullscreen .el-dialog__header {}
.sc-dialog:deep(.el-dialog).is-fullscreen .el-dialog__body {flex:1;overflow: auto;}
.sc-dialog:deep(.el-dialog).is-fullscreen .el-dialog__footer {padding-bottom: 10px;border-top: 1px solid var(--el-border-color-base);}

</style>
