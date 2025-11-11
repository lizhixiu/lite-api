// 生成唯一 ID
let modalIdCounter = 0;
const generateModalId = () => `modal_${Date.now()}_${++modalIdCounter}`;

export default {
	namespaced: true,
	state: {
		modals: [], // Modal 栈，支持多层
		baseZIndex: 2000, // 基础 z-index
	},
	mutations: {
		// 添加 Modal 到栈顶
		ADD_MODAL(state, modal) {
			const id = modal.id || generateModalId();
			const zIndex = state.baseZIndex + state.modals.length * 10;
			state.modals.push({
				id,
				visible: true,
				title: modal.title || '',
				mode: modal.mode || 'dialog',
				width: modal.width || '70%',
				drawerDirection: modal.drawerDirection || 'rtl',
				shade: modal.shade !== undefined ? modal.shade : true,
				showClose: modal.showClose !== undefined ? modal.showClose : true,
				showFullscreen: modal.showFullscreen !== undefined ? modal.showFullscreen : true,
				showFooter: modal.showFooter !== undefined ? modal.showFooter : false,
				buttons: modal.buttons || [],
				src: modal.src || '',
				contentProps: modal.contentProps || {},
				onOpened: modal.onOpened || null,
				onClosed: modal.onClosed || null,
				onConfirm: modal.onConfirm || null,
				zIndex
			});
		},
		// 从栈中移除指定 Modal
		REMOVE_MODAL(state, id) {
			const index = state.modals.findIndex(m => m.id === id);
			if (index !== -1) {
				state.modals.splice(index, 1);
			}
		},
		// 更新 Modal 的 visible 状态
		SET_MODAL_VISIBLE(state, { id, visible }) {
			const modal = state.modals.find(m => m.id === id);
			if (modal) {
				modal.visible = visible;
			}
		},
		// 关闭所有 Modal
		CLEAR_ALL_MODALS(state) {
			state.modals = [];
		},
		// 关闭最顶层的 Modal
		POP_MODAL(state) {
			state.modals.pop();
		}
	},
	getters: {
		// 获取所有可见的 Modal
		visibleModals: (state) => state.modals.filter(m => m.visible),
		// 获取最顶层的 Modal
		topModal: (state) => state.modals.length > 0 ? state.modals[state.modals.length - 1] : null
	},
	actions: {
		// 打开新的 Modal（添加到栈顶）
		// eslint-disable-next-line no-unused-vars
		open({ commit, state }, options = {}) {
			const id = options.id || generateModalId();
			commit('ADD_MODAL', { ...options, id });
			// 返回 Modal ID，方便后续操作
			return id;
		},
		// 关闭指定 ID 的 Modal
		close({ commit, state }, id) {
			if (id) {
				// 关闭指定 Modal
				commit('SET_MODAL_VISIBLE', { id, visible: false });
				// 延迟移除，等待关闭动画完成
				setTimeout(() => {
					commit('REMOVE_MODAL', id);
				}, 300);
			} else {
				// 关闭最顶层的 Modal
				const topModal = state.modals[state.modals.length - 1];
				if (topModal) {
					commit('SET_MODAL_VISIBLE', { id: topModal.id, visible: false });
					setTimeout(() => {
						commit('POP_MODAL');
					}, 300);
				}
			}
		},
		// 关闭所有 Modal
		closeAll({ commit }) {
			commit('CLEAR_ALL_MODALS');
		}
	}
}

