import { getCurrentInstance } from 'vue'

/**
 * 获取 Vuex store 实例
 * @param {Object} app - Vue app 实例
 * @param {Object} instance - 当前组件实例 (this)
 * @returns {Object|null} Vuex store 实例
 */
function getStore(app, instance) {
	// 方法1: 通过组件实例的 $store 访问（最优先）
	if (instance && instance.$store) {
		return instance.$store;
	}
	
	// 方法2: 通过 getCurrentInstance 获取当前组件实例
	const currentInstance = getCurrentInstance();
	if (currentInstance) {
		// 从组件实例的 proxy 获取
		if (currentInstance.proxy && currentInstance.proxy.$store) {
			return currentInstance.proxy.$store;
		}
		// 从 appContext 的全局属性获取
		if (currentInstance.appContext && currentInstance.appContext.config && currentInstance.appContext.config.globalProperties) {
			const store = currentInstance.appContext.config.globalProperties.$store;
			if (store) return store;
		}
		// 从 provides 获取
		if (currentInstance.provides && currentInstance.provides.store) {
			return currentInstance.provides.store;
		}
	}
	
	// 方法3: 从 app 全局属性获取（备用）
	if (app && app.config && app.config.globalProperties && app.config.globalProperties.$store) {
		return app.config.globalProperties.$store;
	}
	
	// 方法4: 尝试从 app 实例直接获取（Vuex 4+）
	if (app && app._context && app._context.provides && app._context.provides.store) {
		return app._context.provides.store;
	}
	
	return null;
}

/**
 * 全局 Modal 工具类
 */
export class GlobalModal {
	constructor(app) {
		this.app = app;
	}

	/**
	 * 打开全局 Modal
	 * @param {Object} options - Modal 配置选项
	 * @param {String} options.title - Modal 标题
	 * @param {String} options.mode - Modal 模式: 'dialog' | 'drawer'，默认 'dialog'
	 * @param {String} options.width - Modal 宽度，默认 '70%'
	 * @param {String} options.drawerDirection - Drawer 方向: 'ltr' | 'rtl' | 'ttb' | 'btt'，默认 'rtl'
	 * @param {Boolean} options.shade - 是否显示遮罩，默认 true
	 * @param {Boolean} options.showClose - 是否显示关闭按钮，默认 true
	 * @param {Boolean} options.showFullscreen - 是否显示全屏按钮，默认 true
	 * @param {Boolean} options.showFooter - 是否显示底部，默认 false
	 * @param {Array} options.buttons - 底部按钮配置
	 * @param {String} options.src - 要加载的组件路径（相对于 views 或 components）
	 * @param {Object} options.contentProps - 传递给内容组件的 props
	 * @param {Function} options.onOpened - 打开后的回调
	 * @param {Function} options.onClosed - 关闭后的回调
	 * @param {Function} options.onConfirm - 确认按钮的回调
	 * @returns {String} Modal ID
	 * @example
	 * // 基本用法
	 * this.$xcModal.open({ title: '标题' })
	 * 
	 * // 加载组件
	 * this.$xcModal.open({
	 *   title: '编辑用户',
	 *   src: 'user/edit',
	 *   contentProps: { userId: 123 }
	 * })
	 * 
	 * // Drawer 模式
	 * this.$xcModal.open({
	 *   title: '设置',
	 *   mode: 'drawer',
	 *   width: '400px'
	 * })
	 */
	open(options = {}, componentInstance) {
		// componentInstance 是从 call(this) 传入的组件实例
		const store = getStore(this.app, componentInstance);
		if (store) {
			return store.dispatch('modal/open', options);
		} else {
			console.error('Store not found. Make sure Vuex store is properly installed and you are calling this method from a component.');
			return null;
		}
	}

	/**
	 * 关闭全局 Modal
	 * @param {String} id - 可选，指定要关闭的 Modal ID。如果不传，则关闭最顶层的 Modal
	 * @example
	 * // 关闭最顶层的 Modal
	 * this.$xcModal.close()
	 * 
	 * // 关闭指定 ID 的 Modal
	 * const modalId = this.$xcModal.open({ title: '标题' })
	 * this.$xcModal.close(modalId)
	 */
	close(id, componentInstance) {
		// componentInstance 是从 call(this) 传入的组件实例
		const store = getStore(this.app, componentInstance);
		if (store) {
			store.dispatch('modal/close', id);
		} else {
			console.error('Store not found. Make sure Vuex store is properly installed and you are calling this method from a component.');
		}
	}

	/**
	 * 关闭所有 Modal
	 * @example
	 * this.$xcModal.closeAll()
	 */
	closeAll(componentInstance) {
		// componentInstance 是从 call(this) 传入的组件实例
		const store = getStore(this.app, componentInstance);
		if (store) {
			store.dispatch('modal/closeAll');
		} else {
			console.error('Store not found. Make sure Vuex store is properly installed and you are calling this method from a component.');
		}
	}
}

/**
 * 创建全局 Modal 实例
 * @param {Object} app - Vue app 实例
 * @returns {GlobalModal} GlobalModal 实例
 */
export function createGlobalModal(app) {
	return new GlobalModal(app);
}

