import {ElMessage, ElMessageBox} from "element-plus";
import apiConfig from "@/config";
import http from "@/utils/request";

const comEvents = {}

comEvents.form = {
	handleFetchData: function (formRef,context) {
		if (context.dataBind) {
			http.post(`${apiConfig.API_URL}` + context.dataBind, context.param).then(res => {
				if (res.code === 200) {
					let form = res.data;
					let sourceData = JSON.parse(JSON.stringify(res.data));
					formRef.setFormData(form,sourceData);
				} else {
					ElMessage.error(res.msg);
				}
			});
		}
	}
	, exportExcel: function () {

	}, handleBatchDelete: function (context) {
		ElMessageBox.confirm(context.confirmMsg || `确定删除选中记录吗？`, '提示', {
			type: 'warning'
		}).then(async () => {
			if (context.deleteAction) {
				const url = `${apiConfig.API_URL}` + context.deleteAction;
				http.post(url, context.param).then((res) => {
					if (res !== null && res.code === 200) {
						ElMessage.success(context.successMsg || '删除成功');
						//成功后：回调刷新
						if (typeof context.afterSuccess === 'function') {
							try {
								const respContext = {
									res,
								}
								context.afterSuccess(respContext);
							} catch (e) {
								console.warn(e);
							}
						}
					}else if (res !== null && res.code !== 200) {
						ElMessage.error(res.message || '删除失败');
					}
				});
			}
		}).catch((action) => {
			// 当用户点击取消时，action 的值为 'cancel'
			if (action === 'cancel') {
				// 可以在这里添加取消操作后的提示，也可以留空不做处理
				// ElMessage.info('已取消操作');
			}
		});
	}, handleDelete: function (context = {}) {
		ElMessageBox.confirm(context.confirmMsg || `确定删除记录吗？`, '提示', {
			type: 'warning'
		}).then(async () => {
			if (context.deleteAction) {
				const url = `${apiConfig.API_URL}` + context.deleteAction;
				http.post(url, context.param).then((res) => {
					if (res !== null && res.code === 200) {
						ElMessage.success(context.successMsg || '删除成功');
						//成功后：回调刷新
						if (typeof context.afterSuccess === 'function') {
							try {
								const respContext = {
									res,
								}
								context.afterSuccess(respContext);
							} catch (e) {
								console.warn(e);
							}
						}
					}else if (res !== null && res.code !== 200) {
						ElMessage.error(res.message || '删除失败');
					}
				});
			}
		}).catch((action) => {
			// 当用户点击取消时，action 的值为 'cancel'
			if (action === 'cancel') {
				// 可以在这里添加取消操作后的提示，也可以留空不做处理
				// ElMessage.info('已取消操作');
			}
		});
	}
	, handleSave: function (view,context = {}) {
		if (!context.actionType)
		{
			ElMessage.error('未指定操作类型');
			return;
		}
		let formRef = view.getFormRef();
		let actionType = context.actionType !== '' ? context.actionType : 'view';
		console.log('actionType',actionType);
		let saveConfig = context.saveConfig;
		console.log('saveConfig',saveConfig);
		let url = '';
		let successMsg = '';
		switch (actionType) {
			case 'add':
				if (saveConfig.insertSuccessMsg) successMsg = saveConfig.insertSuccessMsg;
				if (saveConfig.insertAction) url = saveConfig.insertAction;
				break;
			case 'copy':
				if (saveConfig.copySuccessMsg) successMsg = saveConfig.copySuccessMsg;
				console.log('switch   copy',this.context);
				if (saveConfig.insertAction) url = saveConfig.insertAction;
				console.log('switch   copy url',url);
				break;
			case 'edit':
				if (saveConfig.updateSuccessMsg) successMsg = context.updateSuccessMsg;
				if (saveConfig.updateAction) url = saveConfig.updateAction;
				break;
			case 'post':
				if (saveConfig.postSuccessMsg) successMsg = this.context.postSuccessMsg;
				if (saveConfig.postAction) url = saveConfig.postAction;
				break;
			default:
				break;
		}

		if (url === '') {
			ElMessage.error('未配置接口参数');
			return
		}

		formRef.validate(async (valid) => {
			if (!valid) {
				ElMessage.warning('请填写完整信息');
				return;
			}

			url = `${apiConfig.API_URL}` + url;

			let dataToSubmit;
			if (actionType === "add" || actionType === "copy" || actionType === "post") {
				// 新增或拷贝操作提交完整表单数据
				dataToSubmit = view.getFormData();
			}else if (actionType === "edit") {

				dataToSubmit = this.getChangedData(view.getSourceData(),view.getFormData());
				// 如果没有变化，提示用户并返回
				if (Object.keys(dataToSubmit).length === 0) {
					ElMessage.info('数据未发生变化，无需提交');
					this.isSaveing = false;
					return;
				}

				// 编辑操作提交主键ID
				dataToSubmit.id = view.getFormData().id;

				if(context.updateParamsKey){
					context.updateParamsKey.forEach((key) => {
						dataToSubmit[key] = view.getFormData()[key];
					});
				}
			}

			await http.post(url, dataToSubmit, {
				headers: {
					//'response-status': 401
				}
			}).then((res) => {
				if (res !== null && res.code === 200) {
					ElMessage.success(successMsg || "操作成功")
					//成功后：回调刷新
					if (typeof context.afterSuccess === 'function') {
						try {
							const respContext = {
								actionType, res,
							}
							context.afterSuccess(respContext);
						} catch (e) {
							console.warn(e);
						}
					}
				} else {
					// console.log("<UNK>", res);
					ElMessage.error(res.message || '操作失败');
					return;
				}
			}).catch(() => {
				// ElMessage.error("操作失败");
			});
		})
	}
	,
	// 对比数据，找出有变化的字段
	getChangedData(sourceData,formData) {
		const changedData = {};
		for (const key in formData) {
			// eslint-disable-next-line no-prototype-builtins
			if (formData.hasOwnProperty(key)) {
				const formValue = formData[key];
				const sourceValue = sourceData[key];
				if (JSON.stringify(formValue) !== JSON.stringify(sourceValue)) {
					changedData[key] = formValue;
				}
			}
		}
		return changedData;
	},
}
comEvents.lay = {
	handleOpen: function (view,context) {
		console.log(view.$xcModal);
		view.$xcModal.open({
			title: context.title,
			src: context.src,
			width: '80%',
			contentProps: {
				actionType: context.actionType,
				dataBind: context.dataBind,
				param: context.param,
				afterSuccess: context.afterSuccess,
			}
		})
	},
}
comEvents.base = {
	handleRefresh: function (ref) {
		// console.log('handleRefresh:', ref);
		if (ref && typeof ref.refresh === 'function') {
			ref.refresh()
		}
	},
}
export default comEvents
