export default {
	data() {
		return {
			processLoading: false
		}
	},
	methods: {
		async handleProcessApproval(tableId, formId) {
			if (this.processLoading) return
			try {
				this.processLoading = true
				await this.$refs['process'].save(tableId, formId)
				// this.$baseMessage('提交成功', 'success')
			} catch (error) {
				console.error('提交审批失败:', error)
				// this.$baseMessage('提交失败', 'error')
			} finally {
				this.processLoading = false
			}
		}
	}
}