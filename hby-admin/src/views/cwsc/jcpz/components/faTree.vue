<template>
  <div class="fa-tree">
    <div class="fa-tree-header">
      <el-input v-model="fname" placeholder="方案名称" />
      <el-button type="primary" @click="search" style="margin-left: 10px">
        查询
      </el-button>
      <el-button type="primary" @click="reset" style="margin-left: 10px">
        重置
      </el-button>
    </div>
    <div class="fa-tree-body">
      <div class="fa-tree-item" v-for="item in data" :key="item.fid">
        <div
          class="fa-tree-item-header"
          :class="{ 'fa-tree-item-header-selected': selected === item.fid }"
          @click="handleSelect(item)"
        >
          <span class="ing" v-if="item.record && item.record.iscompleted === 1">
            进行中
          </span>
          <span
            class="done"
            v-if="item.record && item.record.iscompleted === 2"
          >
            已完成
          </span>
          <span
            class="wait"
            v-if="item.record && item.record.iscompleted === 3"
          >
            等待中
          </span>
          <span class="danger" v-if="!item.record">未采集</span>
          <span>{{ item.fname }}</span>
          <span class="fa-tree-item-header-right">
            <el-popover
              placement="bottom"
              width="30"
              trigger="click"
              style="width: 30px"
            >
              <div>
                <el-button
                  type="text"
                  @click="handleBegin(item)"
                  :disabled="item.record && item.record.iscompleted === 1"
                >
                  开始抽取
                </el-button>
                <el-button type="text" @click="handleStop(item)">
                  停止抽取
                </el-button>
              </div>
              <el-button slot="reference" type="text">操作</el-button>
            </el-popover>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { getFaTree, startFaGather, stopFaGather } from '@/api/cwsc'
  export default {
    name: 'faTree',
    data() {
      return {
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        fname: '',
        selected: '',
      }
    },
    created() {
      this.getFaTree()
    },
    methods: {
      getFaTree() {
        getFaTree({ fname: this.fname }).then((res) => {
          this.data = res.data
        })
      },
      search() {
        this.getFaTree()
      },
      reset() {
        this.fname = ''
        this.getFaTree()
      },
      handleBegin(item) {
        if (!item.record) {
          this.$message.warning('方案未采集，请先采集')
          return
        }
        startFaGather({ fid: item.fid }).then((res) => {
          this.$message.success('开始采集')
          this.getFaTree()
        })
      },
      handleStop(item) {
        if (!item.record) {
          this.$message.warning('方案未采集，请先采集')
          return
        }
        stopFaGather({ planid: item.fid }).then((res) => {
          this.$message.success('停止采集')
          this.getFaTree()
        })
      },
      handleSelect(item) {
        this.selected = item.fid
        this.$emit('select', item)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .fa-tree {
    width: 100%;
  }
  .ing {
    color: #409eff;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #409eff;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .done {
    color: #67c23a;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #67c23a;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .wait {
    color: #909399;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #909399;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .danger {
    color: #f56c6c;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #f56c6c;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .fa-tree-item-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
    border: 1px solid #e6e6e6;
    cursor: pointer;
    margin-top: 20px;
    &:hover {
      background-color: #f5f7fa;
    }
  }
  .fa-tree-item-header-left {
    display: flex;
    align-items: center;
    margin-right: 10px;
  }
  .fa-tree-body {
    height: 100%;
    overflow-y: auto;
  }
  >>> .el-popover {
    min-width: 70px !important;
  }
  .fa-tree-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
  }
  >>> .el-checkbox {
    margin-right: 0;
  }
  >>> .el-checkbox__input {
    margin-right: 0;
  }
  >>> .el-checkbox__label {
    display: none;
  }
  .fa-tree-item-header-selected {
    background-color: #f5f7fa;
    border: 1px solid #409eff;
  }
</style>
