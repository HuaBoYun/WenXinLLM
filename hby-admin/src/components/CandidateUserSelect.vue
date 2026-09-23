<template>
  <div class="popupSelect-container">
    <div class="el-select" @click.stop="openDialog">
      <div
        class="el-select__tags"
        v-if="multiple"
        ref="tags"
        :style="{
          'max-width': inputWidth - 32 + 'px',
          width: '100%',
          cursor: 'pointer',
        }"
      >
        <span v-if="collapseTags && tagsList.length">
          <el-tag
            :closable="!selectDisabled"
            :size="collapseTagSize"
            type="info"
            @close="deleteTag($event, 0)"
            disable-transitions
          >
            <span class="el-select__tags-text">{{ tagsList[0].fullName }}</span>
          </el-tag>
          <el-tag
            v-if="tagsList.length > 1"
            :closable="false"
            type="info"
            disable-transitions
          >
            <span class="el-select__tags-text">
              + {{ tagsList.length - 1 }}
            </span>
          </el-tag>
        </span>
        <transition-group @after-leave="resetInputHeight" v-if="!collapseTags">
          <el-tag
            v-for="(item, i) in tagsList"
            :key="item.id"
            :size="collapseTagSize"
            :closable="!selectDisabled"
            type="info"
            @close="deleteTag($event, i)"
            disable-transitions
          >
            <span class="el-select__tags-text">{{ item.fullName }}</span>
          </el-tag>
        </transition-group>
      </div>
      <el-input
        ref="reference"
        v-model="innerValue"
        type="text"
        :placeholder="currentPlaceholder"
        :disabled="selectDisabled"
        readonly
        :validate-event="false"
        :tabindex="multiple ? '-1' : null"
        @mouseenter.native="setValue()"
        @mouseleave.native="inputHovering = false"
      >
        <template slot="suffix">
          <i
            v-show="!showClose"
            :class="['el-select__caret', 'el-input__icon', 'el-icon-arrow-up']"
          ></i>
          <i
            v-if="showClose"
            class="el-select__caret el-input__icon el-icon-circle-close"
            @click="handleClearClick"
          ></i>
        </template>
      </el-input>
    </div>
    <el-dialog
      title="候选人员"
      :close-on-click-modal="false"
      :visible.sync="visible"
      class="transfer-dialog"
      lock-scroll
      append-to-body
      width="800px"
      :modal-append-to-body="false"
      @close="onClose"
    >
      <div class="transfer__body">
        <div class="transfer-pane">
          <div class="transfer-pane__tools">
            <el-input
              placeholder="请输入关键词查询"
              v-model="queryForm.keyword"
              @keyup.enter.native="search"
              clearable
              class="search-input"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="search"
              ></el-button>
            </el-input>
            <el-button @click="setAll" type="text" class="removeAllBtn">
              全选
            </el-button>
          </div>
          <div class="transfer-pane__body left-pane">
            <div class="custom-title">全部数据</div>

            <div class="single-list" ref="candidate">
              <template v-if="list.length">
                <div
                  v-for="(item, index) in list"
                  :key="index"
                  class="selected-item-user"
                  @click="handleNodeClick(item)"
                >
                  <div class="selected-item-main">
                    <!-- <el-avatar
                      :size="36"
                      :src="define.comUrl + item.headIcon"
                      class="selected-item-headIcon"
                    ></el-avatar> -->
                    <el-avatar
                      :size="36"
                      src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
                      class="selected-item-headIcon"
                    ></el-avatar>
                    <div class="selected-item-text">
                      <p class="name">{{ item.fullName }}</p>
                      <p class="organize" :title="item.organize">
                        {{ item.organize }}
                      </p>
                    </div>
                  </div>
                </div>
              </template>
              <el-empty
                description="暂无数据"
                :image-size="120"
                v-else
              ></el-empty>
            </div>
          </div>
        </div>
        <div class="transfer-pane">
          <div class="transfer-pane__tools">
            <span>已选</span>
            <el-button @click="removeAll" type="text" class="removeAllBtn">
              清空列表
            </el-button>
          </div>
          <div class="transfer-pane__body shadow right-pane">
            <template v-if="selectedData.length">
              <div
                v-for="(item, index) in selectedData"
                :key="index"
                class="selected-item-user"
              >
                <div class="selected-item-main">
                  <!-- <el-avatar
                    :size="36"
                    :src="define.comUrl + item.headIcon"
                    class="selected-item-headIcon"
                  ></el-avatar> -->
                  <el-avatar
                    :size="36"
                    src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
                    class="selected-item-headIcon"
                  ></el-avatar>
                  <div class="selected-item-text">
                    <p class="name">
                      {{ item.fullName }}
                      <i class="el-icon-delete" @click="removeData(index)"></i>
                    </p>
                    <p class="organize" :title="item.organize">
                      {{ item.organize }}
                    </p>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              description="暂无数据"
              :image-size="120"
              v-else
            ></el-empty>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="confirm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  // import { CandidateUser } from '@/api/workFlow/FlowBefore'
  // import { getUserInfoList } from '@/api/permission/user'
  import { deWeight } from '@/utils/index'
  import { getCandidatesList } from '@/api/setting/system'
  import {
    getBatchList,
    batchCandidate,
    batchCandidateUser,
  } from '@/oapi/contract/manage'
  import {
    addResizeListener,
    removeResizeListener,
  } from 'element-ui/src/utils/resize-event'
  export default {
    name: 'CandidateUser',
    inject: {
      elForm: {
        default: '',
      },
      elFormItem: {
        default: '',
      },
    },
    props: {
      value: {
        type: [String, Array],
        default: '',
      },
      index: {
        type: Number,
        default: 0,
      },
      placeholder: {
        type: String,
        default: '请选择',
      },
      disabled: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: true,
      },
      collapseTags: {
        type: Boolean,
        default: false,
      },
      clearable: {
        type: Boolean,
        default: true,
      },
      taskId: {
        type: String,
        default: '',
      },
      nodeId: {
        type: String,
        default: '',
      },
      formData: {
        type: Object,
        default: () => ({}),
      },
      candidateData: {
        type: Object,
        default: () => {},
      },
      size: String,
      clearType: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        visible: false,
        innerValue: '',
        loading: false,
        total: 0,
        list: [],
        listLoading: false,
        selectedData: [],
        tagsList: [],
        inputHovering: false,
        inputWidth: 0,
        initialInputHeight: 0,
        finish: false,
        queryForm: {
          keyword: '',
          tableId: '',
          fromId: '',
          nodeCode: '',
          currentPage: 1,
          pageSize: 20,
        },
      }
    },
    watch: {
      value(val) {
        // this.setDefault()
      },
      selectDisabled() {
        this.$nextTick(() => {
          this.resetInputHeight()
        })
      },
      clearType(val) {
        if (val) {
          this.tagsList = []
          this.selectedData = []
        }
      },
    },
    computed: {
      showClose() {
        let hasValue = this.multiple
          ? Array.isArray(this.value) && this.value.length > 0
          : this.value !== undefined && this.value !== null && this.value !== ''
        let criteria =
          this.clearable &&
          !this.selectDisabled &&
          this.inputHovering &&
          hasValue
        return criteria
      },
      currentPlaceholder() {
        if (this.multiple && Array.isArray(this.value) && this.value.length) {
          return ''
        } else {
          return this.placeholder
        }
      },
      selectDisabled() {
        return this.disabled || (this.elForm || {}).disabled
      },
      _elFormItemSize() {
        return (this.elFormItem || {}).elFormItemSize
      },
      selectSize() {
        return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size
      },
      collapseTagSize() {
        return ['small', 'mini'].indexOf(this.selectSize) > -1
          ? 'mini'
          : 'small'
      },
    },
    created() {
      // this.setDefault()
    },
    mounted() {
      this.$bus.on('candidate', (value) => {
        if (value == 1) {
          this.innerValue = ''
        }
      })

      addResizeListener(this.$el, this.handleResize)

      const reference = this.$refs.reference
      if (reference && reference.$el) {
        const sizeMap = {
          medium: 36,
          small: 32,
          mini: 28,
        }
        const input = reference.$el.querySelector('input')
        this.initialInputHeight =
          input.getBoundingClientRect().height || sizeMap[this.selectSize]
      }
      if (this.multiple) {
        this.resetInputHeight()
      }
      this.$nextTick(() => {
        if (reference && reference.$el) {
          this.inputWidth = reference.$el.getBoundingClientRect().width
        }
      })
      // this.setDefault()
    },
    beforeDestroy() {
      if (this.$el && this.handleResize)
        removeResizeListener(this.$el, this.handleResize)
    },
    methods: {
      setValue() {
        if (this.candidateData) {
          console.log('this.candidateData', this.candidateData)
          this.queryForm.tableId = this.candidateData.tableId || ''
          this.queryForm.flowId = this.candidateData.flowId || ''
          this.queryForm.fromId = this.candidateData.fromId
          this.queryForm.id = this.candidateData.id
          this.queryForm.flowTaskOperatorId =
            this.candidateData.flowTaskOperatorId
          this.queryForm.batch = this.candidateData.batch
          this.queryForm.nodeCode = this.nodeId
          this.queryData()
        }
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        if (this.queryForm.batch) {
          const { data, code } = await batchCandidateUser({
            flowId: this.queryForm.flowId,
            id: this.queryForm.id,
            nodeCode: this.queryForm.nodeCode,
            currentPage: 1,
            pageSize: 9999,
          })
          if (code == 1) {
            let list2 = [...this.list, ...data.list]
            this.list = deWeight(list2)
            this.total = data.totalCount + 1
            this.inputHovering = true
            this.listLoading = false
          }
        } else {
          const {
            data: { list, totalCount },
          } = await getCandidatesList(this.queryForm)
          // this.list = list
          let list2 = [...this.list, ...list]
          this.list = deWeight(list2)
          this.total = totalCount + 1
          this.inputHovering = true
          this.listLoading = false
        }
      },
      onClose() {
        this.list = []
      },
      clear() {
        if (this.disabled) return
        this.innerValue = ''
        this.selectedData = []
        this.$emit('input', '')
        this.$emit('change', '', '')
      },
      openDialog() {
        if (this.disabled) return
        this.visible = true
        this.finish = false
        this.queryForm.keyword = ''
        this.$nextTick(() => {
          this.bindScroll()
          this.search()
          // this.setDefault()

          if (this.$store.state.work.processMobile) {
            const transDialog =
              document.getElementsByClassName('transfer-dialog')[0]
            const dialogBody =
              transDialog.getElementsByClassName('el-dialog__body')[0]
            dialogBody.style.cssText = 'overflow-y: auto;'
          }
        })
      },
      confirm() {
        if (this.multiple) {
          this.innerValue = ''
          this.tagsList = JSON.parse(JSON.stringify(this.selectedData))
          let selectedIds = this.selectedData.map((o) => o.id)
          this.list = []
          this.$emit('input', selectedIds)
          this.$emit('change', selectedIds, this.selectedData)
          this.$emit('selected', this.index, this.selectedData)
        } else {
          if (!this.selectedData.length) {
            this.innerValue = ''
            this.$emit('input', '')
            this.$emit('change', '', {})
            this.visible = false
            return
          }
          this.innerValue = this.selectedData[0].fullName
          let selectedIds = this.selectedData.map((o) => o.id)
          this.$emit('input', selectedIds[0])
          this.$emit('change', selectedIds[0], this.selectedData[0])
        }
        this.visible = false
      },
      bindScroll() {
        let _this = this,
          vBody = _this.$refs.candidate
        vBody.addEventListener('scroll', function () {
          if (
            vBody.scrollHeight - vBody.clientHeight - vBody.scrollTop <= 200 &&
            !_this.listLoading &&
            !_this.finish
          ) {
            _this.queryForm.currentPage += 1
            _this.queryData()
          }
        })
      },
      search() {
        this.queryForm.currentPage = 1
        this.queryForm.pageSize = 20
        this.list = []
        this.finish = false
        // this.initData()
        this.queryData()
      },
      initData() {
        this.listLoading = true
        let query = {
          ...this.queryForm,
          formData: this.formData,
          nodeCode: this.nodeId,
        }
        // CandidateUser(this.taskId || 0, query).then((res) => {
        //   if (res.data.list.length < this.queryForm.pageSize) {
        //     this.finish = true
        //   }
        //   this.list = [...this.list, ...res.data.list]
        //   this.total = res.data.pagination.total
        //   this.listLoading = false
        // })
      },
      handleNodeClick(item) {
        const boo = this.selectedData.some((o) => o.id === item.id)
        if (boo) return
        this.multiple
          ? this.selectedData.push(item)
          : (this.selectedData = [item])
      },
      removeAll() {
        this.selectedData = []
      },
      setAll() {
        this.selectedData = this.list
      },
      removeData(index) {
        this.selectedData.splice(index, 1)
      },
      setDefault() {
        if (!this.value || !this.value.length) {
          this.innerValue = ''
          this.selectedData = []
          this.tagsList = []
          return
        }
        const arr = this.multiple ? this.value : [this.value]
        getUserInfoList(arr).then((res) => {
          this.selectedData = res.data.list
          if (this.multiple) {
            this.innerValue = ''
            this.tagsList = JSON.parse(JSON.stringify(this.selectedData))
          } else {
            this.innerValue = this.selectedData.length
              ? this.selectedData[0].fullName
              : ''
          }
          this.$nextTick(() => {
            if (this.multiple) {
              this.resetInputHeight()
            }
          })
        })
      },
      deleteTag(event, index) {
        this.selectedData.splice(index, 1)
        this.confirm()
        event.stopPropagation()
      },
      handleClearClick(event) {
        this.selectedData = []
        this.confirm()
        event.stopPropagation()
      },
      resetInputWidth() {
        this.inputWidth = this.$refs.reference.$el.getBoundingClientRect().width
      },
      handleResize() {
        this.resetInputWidth()
        if (this.multiple) this.resetInputHeight()
      },
      resetInputHeight() {
        if (this.collapseTags) return
        this.$nextTick(() => {
          if (!this.$refs.reference) return
          let inputChildNodes = this.$refs.reference.$el.childNodes
          let input = [].filter.call(
            inputChildNodes,
            (item) => item.tagName === 'INPUT'
          )[0]
          const tags = this.$refs.tags
          const tagsHeight = tags
            ? Math.round(tags.getBoundingClientRect().height)
            : 0
          const sizeInMap = this.initialInputHeight || 40
          input.style.height =
            this.selectedData.length === 0
              ? sizeInMap + 'px'
              : Math.max(
                  tags ? tagsHeight + (tagsHeight > sizeInMap ? 6 : 0) : 0,
                  sizeInMap
                ) + 'px'
        })
      },
      resetInputWidth() {
        this.inputWidth = this.$refs.reference.$el.getBoundingClientRect().width
      },
      handleResize() {
        this.resetInputWidth()
        if (this.multiple) this.resetInputHeight()
      },
    },
  }
</script>
<style lang="scss" scoped>
  .popupSelect-container {
    width: 100%;

    .popover-container {
      width: 100%;

      .el-popover__reference-wrapper,
      .el-select {
        width: 100%;
      }
    }

    & > .el-select {
      width: 100%;
    }
  }
  .transfer__body {
    line-height: 32px;
    display: flex;
    justify-content: space-around;
    padding-top: 10px;
    height: 400px;
    flex-wrap: wrap;

    .transfer-pane {
      width: 300px;
      height: 100%;
    }

    .transfer-pane__tools {
      margin-bottom: 8px;
      height: 32px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .removeAllBtn {
        color: #f56c6c;
      }
    }

    .transfer-pane__body {
      position: relative;
      width: 100%;
      height: calc(100% - 40px);
      overflow: auto;
      overflow-x: hidden;
      font-size: 14px;
      border: 1px solid #dcdfe6;
      border-radius: 4px;

      .el-tab-pane {
        padding: 10px 0 !important;
      }

      &.left-pane {
        .custom-title {
          height: 39px;
          padding: 0 12px;
          line-height: 39px;
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          border-bottom: 1px solid #dcdfe6;

          & + .single-list {
            height: calc(100% - 40px);
          }
        }

        .single-list {
          height: 100%;
          overflow: auto;
          overflow-x: hidden;

          .selected-item-user {
            cursor: pointer;
          }

          .selected-item {
            padding: 0px 12px;
            cursor: pointer;

            &:hover {
              background-color: #f5f7fa;
            }
          }
        }
      }
    }

    .selected-item-user {
      padding: 0 12px;

      &:hover {
        background-color: #f5f7fa;
      }

      .selected-item-main {
        border-bottom: 1px solid #f4f6f9;
        display: flex;
        align-items: center;
        height: 50px;
      }

      .selected-item-headIcon {
        flex-shrink: 0;
      }

      .selected-item-text {
        // width: 272px;
        width: 230px;
        margin-left: 10px;

        .name {
          height: 20px;
          line-height: 20px;
          font-size: 14px;
          margin-bottom: 2px;
          display: flex;
          align-items: center;
          justify-content: space-between;
        }

        .organize {
          height: 17px;
          line-height: 17px;
          color: #999999;
          font-size: 12px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .el-icon-delete:hover {
          color: #f56c6c;
          cursor: pointer;
        }
      }
    }

    .right-pane {
      box-sizing: border-box;
      overflow: auto;
      border: 1px solid #dcdfe6;

      .selected-item-user {
        .organize {
          padding-right: 25px;
        }
      }

      .selected-item {
        padding: 0px 12px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        &:hover {
          background-color: #f5f7fa;
        }

        span {
          max-width: 90%;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .el-icon-delete:hover {
          color: #f56c6c;
          cursor: pointer;
        }
      }
    }
  }
</style>
