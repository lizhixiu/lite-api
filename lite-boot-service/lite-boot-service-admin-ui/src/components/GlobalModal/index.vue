<template>
	<!-- 全局 xcModal - 支持多层 -->
	<xcModal
		v-for="modal in modals"
		:key="modal.id"
		:ref="el => setModalRef(modal.id, el)"
		:model-value="modal.visible"
		:title="modal.title"
		:mode="modal.mode"
		:width="modal.width"
		:drawer-direction="modal.drawerDirection"
		:shade="modal.shade"
		:show-close="modal.showClose"
		:show-fullscreen="modal.showFullscreen"
		:show-footer="modal.showFooter"
		:buttons="modal.buttons"
		:src="modal.src"
		:z-index="modal.zIndex"
		@update:model-value="(val) => handleModalVisibleChange(modal.id, val)"
		@opened="() => handleModalOpened(modal)"
		@closed="() => handleModalClosed(modal)"
		@confirm="(data) => handleModalConfirm(modal, data)"
	>
		<template v-if="!modal.src" #default>
			<slot name="modal-content"></slot>
		</template>
	</xcModal>
</template>

<script>
import xcModal from '@/components/xc/feedback/xcModal/index.vue'

export default {
	name: 'GlobalModal',
	components: {
		xcModal
	},
	data() {
		return {
			modalRefs: {} // 存储每个 Modal 的 ref
		}
	},
	computed: {
		// Modal 列表
		modals() {
			return this.$store.state.modal.modals || []
		}
	},
	watch: {
		modals: {
			handler(newModals) {
				// 当 Modal 打开时，设置 contentProps
				this.$nextTick(() => {
					newModals.forEach(modal => {
						if (modal.visible && this.modalRefs[modal.id]) {
							const contentProps = modal.contentProps
							if (contentProps && Object.keys(contentProps).length > 0) {
								this.modalRefs[modal.id].setContentProps(contentProps)
							}
						}
					})
				})
			},
			deep: true,
			immediate: true
		}
	},
	methods: {
		setModalRef(id, el) {
			if (el) {
				this.modalRefs[id] = el
			} else {
				delete this.modalRefs[id]
			}
		},
		handleModalVisibleChange(modalId, visible) {
			if (!visible) {
				this.$store.dispatch('modal/close', modalId)
			}
		},
		handleModalOpened(modal) {
			// 当 Modal 打开时，设置 contentProps
			this.$nextTick(() => {
				if (this.modalRefs[modal.id]) {
					const contentProps = modal.contentProps
					if (contentProps && Object.keys(contentProps).length > 0) {
						this.modalRefs[modal.id].setContentProps(contentProps)
					}
				}
			})
			if (modal.onOpened) {
				modal.onOpened()
			}
		},
		handleModalClosed(modal) {
			if (modal.onClosed) {
				modal.onClosed()
			}
		},
		handleModalConfirm(modal, data) {
			if (modal.onConfirm) {
				modal.onConfirm(data)
			}
		}
	}
}
</script>

