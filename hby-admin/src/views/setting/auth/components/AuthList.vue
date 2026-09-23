<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <vab-query-form>
        <vab-query-form-left-panel :span="12">
          <div class="right-wrapper">
            <div class="title">功能授权</div>
            <el-button
              native-type="submit"
              type="primary"
              @click="handleRight('tree', 0)"
            >
              授权
            </el-button>
          </div>
          <div class="tree-wrapper">
            <el-tree
              ref="tree"
              v-loading="loading"
              :data="data"
              :default-checked-keys="checkedKeys"
              :default-expanded-keys="expandedKeys"
              :expand-on-click-node="false"
              node-key="id"
              :props="defaultProps"
              show-checkbox
              @check-change="handleCheckChange"
              @node-click="handleNodeClick"
            />
          </div>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel :span="12">
          <div class="right-wrapper">
            <div class="title">报表授权</div>
            <el-button
              native-type="submit"
              type="primary"
              @click="handleRight('tree2', 1)"
            >
              授权
            </el-button>
          </div>
          <div class="tree-wrapper">
            <el-tree
              ref="tree2"
              v-loading="loading2"
              :data="data2"
              :default-checked-keys="checkedKeys2"
              :default-expanded-keys="expandedKeys2"
              :expand-on-click-node="false"
              node-key="id"
              :props="defaultProps"
              show-checkbox
              @check-change="handleCheckChange"
              @node-click="handleNodeClick"
            />
          </div>
        </vab-query-form-right-panel>
      </vab-query-form>

      <template #footer>
        <el-button @click="close">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import {
    qxsdFeatureList,
    qxsdFeatureSave,
    qxsdTableList,
    qxsdTableSave,
  } from '@/api/setting/auth'
  export default {
    name: 'AuthList',
    data() {
      return {
        title: '授权',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        loading: false,
        loading2: false,
        data: [],
        data2: [],
        checkedKeys: [],
        expandedKeys: [],
        checkedKeys2: [],
        expandedKeys2: [],
        form: {
          userid: '',
          priid: '',
        },
      }
    },
    created() {},
    methods: {
      handleRight(tree, type) {
        // 半选状态的item需要将check设置为true，再传给后端
        const halfChecked = this.$refs[tree].getHalfCheckedKeys()
        const checked = this.$refs[tree].getCheckedKeys()
        this.form.priid = halfChecked.concat(checked).join(',')
        const func = type === 0 ? qxsdFeatureSave : qxsdTableSave
        func(this.form).then((res) => {
          console.log(res)
          if (res.code == 1) {
            this.$message.success('授权成功')
          }
        })
      },
      handleNodeClick(data) {
        console.log(data)
      },
      handleCheckChange(data, checked, indeterminate) {
        console.log(data, checked, indeterminate)
      },
      showEdit(user) {
        this.dialogFormVisible = true
        this.form.userid = user.staffid
        this.fetchData(user, 1)
        this.fetchData(user, 2)
      },
      fetchData(user, index) {
        // 注意：半选状态后端的checked为true，这里需要筛选出最里层子级里checked为true的item设置为默认选中值
        function getLeafCheckedKeys(data) {
          let arr = []
          const acc = (data) => {
            data.forEach((i) => {
              if (i.children && i.children.length) {
                acc(i.children)
              } else {
                if (i.checked) {
                  arr = arr.concat(i.id)
                }
              }
            })
            return arr
          }
          return acc(data)
        }
        const getAllKeys = (data, key) => {
          let arr = []
          data.forEach((i) => {
            if (i[key]) arr.push(i.id)
            if (i.children && i.children.length) {
              arr = arr.concat(getAllKeys(i.children, key))
            }
          })
          return arr
        }
        if (index === 1) {
          this.loading = true
          qxsdFeatureList({
            staffid: user.staffid,
          })
            .then((res) => {
              this.data = res
              this.checkedKeys = getLeafCheckedKeys(this.data)
              this.expandedKeys = getAllKeys(this.data, 'open')
            })
            .finally(() => {
              this.loading = false
            })
        }
        if (index === 2) {
          this.loading2 = true
          qxsdTableList({
            staffid: user.staffid,
          })
            .then((res) => {
              this.data2 = res
              this.checkedKeys2 = getLeafCheckedKeys(this.data2)
              this.expandedKeys2 = getAllKeys(this.data2, 'open')
            })
            .finally(() => {
              this.loading2 = false
            })
        }
      },
      close() {
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .right-wrapper,
  .tree-wrapper {
    width: 95%;
    display: flex;
    //border: 1px red solid;
  }
  .right-wrapper {
    justify-content: space-between;
    align-items: center;
    padding-bottom: 10px;
    margin-bottom: 10px;
    border-bottom: 1px #d5d5d5 solid;
    .title {
      height: 100%;
      display: flex;
    }
  }
  ::v-deep .el-tree {
    width: 100%;
  }
</style>
