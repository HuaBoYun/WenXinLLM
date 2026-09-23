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
            <el-select
              v-model="moduletype"
              placeholder="请选择模块"
              @change="fetchData(1)"
            >
              <!-- <el-option label="系统设置" value="xtsz" />
              <el-option label="合同管理" value="htgl" /> -->
              <!-- <el-option label="法务管理" value="fwgl" />
              <el-option label="智能监控" value="znjk" />
              <el-option label="内控合规" value="nkhg" />
              <el-option label="内部审计" value="znsj" />
              <el-option label="智能分析" value="znfx" />
              <el-option label="风险管控" value="fxgk" /> -->
              <el-option
                v-for="item in moduleLists"
                :key="item.id"
                :label="item.projectName"
                :value="item.uniqueIdentification"
              />
            </el-select>
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
            <div class="title">数据授权</div>
            <div>
              <el-button
                native-type="submit"
                type="primary"
                @click="handleRight('tree2', 1)"
              >
                授权
              </el-button>
              <el-button native-type="submit" @click="handleRightCancel()">
                取消授权
              </el-button>
            </div>
          </div>
          <div class="tree-wrapper">
            <el-tree
              ref="tree2"
              v-loading="loading2"
              :data="data2"
              :expand-on-click-node="false"
              node-key="rightId"
              :props="defaultPropsScreen"
              @check-change="handleCheckChange"
              @node-click="handleNodeClick"
            />
          </div>
        </vab-query-form-right-panel>
      </vab-query-form>

      <template #footer>
        <el-button @click="close">取 消</el-button>
      </template>

      <depart-ment-dialog
        ref="depart"
        @select="handleSelectDep"
        :isMultiple="true"
      />

      <RoleAuthCancelList
        ref="roleAuthCancelList"
        @selected="handleRoleAuthCancelList"
      />
      <ProcessList ref="process" @fetchData="fetchData" />
    </el-dialog>
  </div>
</template>

<script>
  import DepartMentDialog from '@/views/setting/auth/components/DepartMentDialogTree'
  import RoleAuthCancelList from '@/views/setting/auth/components/RoleAuthCancelList'
  import {
    getRoleRightList,
    getRoleRightListScreen,
    grantRoleRight,
    saveRightInfoScreen,
    grantRoleDataRight,
    getRoleDataDeptInfo,
    removeDataRight,
  } from '@/api/setting/auth'
  import { getModuleList } from '@/api/setting/system'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'RoleAuthList',
    components: { DepartMentDialog, RoleAuthCancelList, ProcessList },
    data() {
      return {
        title: '授权',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        defaultPropsScreen: {
          children: 'deptList',
          label: 'orgname',
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
          roleId: '',
          rightIds: '',
          moduleType: 'xtsz',
        },
        role: {}, //角色
        moduletype: 'xtsz',
        // typeList:[],
        moduleLists: [],
        requireValuedata: false,
      }
    },
    created() {
      this.moduleList()
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      handleRightCancel() {
        this.$refs.roleAuthCancelList.show(this.form.roleId)
      },
      handleRoleAuthCancelList(val) {
        console.log('val', val)
        // 对象数组 合并相同公司  部门id  逗号分割
        let map = {}
        val.forEach((item) => {
          if (!map[item.fatherorgid]) {
            map[item.fatherorgid] = []
          }
          map[item.fatherorgid].push(item.orgid)
        })

        let arr2 = Object.entries(map).map((item) => {
          return {
            orgId: item[0],
            deptId: item[1].join(','),
          }
        })

        console.log('arr2', arr2)
        removeDataRight({
          roleId: this.form.roleId,
          dataJson: JSON.stringify(arr2),
        }).then((res) => {
          if (res.code == 1) {
            this.$message.success('取消成功')
            this.fetchData(2)
          }
        })
      },
      handleRight(tree, type) {
        if (type == 0) {
          // 半选状态的item需要将check设置为true，再传给后端
          const halfChecked = this.$refs[tree].getHalfCheckedKeys()
          const checked = this.$refs[tree].getCheckedKeys()
          this.form.rightIds = halfChecked.concat(checked).join(',')
          this.form.moduleType = this.moduletype
          const func = type === 0 ? grantRoleRight : saveRightInfoScreen
          func(this.form).then((res) => {
            console.log(res)
            const { msg, data } = res
            // 流程校验
            if (this.requireValuedata) {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'grant',
                operationType: 11,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else {
              if (res.code == 1) {
                this.$message.success('授权成功')
              }
            }
          })
        } else {
          this.$refs.depart.show(null, this.form.roleId)
        }
      },
      getRightInfo() {
        getRoleDataDeptInfo({ roleId: this.form.roleId }).then((res) => {
          if (res.code == 1) {
            this.data2 = res.data
          }
        })
      },
      sortArr(arr, str) {
        var _arr = [],
          _t = [],
          // 临时的变量
          _tmp

        // 按照特定的参数将数组排序将具有相同值得排在一起
        arr = arr.sort(function (a, b) {
          var s = a[str],
            t = b[str]

          return s < t ? -1 : 1
        })

        if (arr.length) {
          _tmp = arr[0][str]
        }
        // console.log( arr );
        // 将相同类别的对象添加到统一个数组
        for (var i in arr) {
          // console.log( _tmp);
          if (arr[i][str] === _tmp) {
            console.log(_tmp)
            _t.push(arr[i])
          } else {
            _tmp = arr[i][str]
            _arr.push(_t)
            _t = [arr[i]]
          }
        }
        // 将最后的内容推出新数组
        _arr.push(_t)
        return _arr
      },
      handleSelectDep(data) {
        let companyIds = []
        data.map((item) => {
          let obj = { companyId: item.companyId, orgid: item.orgid }
          companyIds.push(obj)
        })
        let list = this.sortArr(companyIds, 'companyId')
        let company = ''
        list.map((item) => {
          company += item[0].companyId + '~'
          item.map((item2) => {
            company += item2.orgid + ','
          })
          company = company.substring(0, company.length - 1)
          company += '-'
        })
        company = company.substring(0, company.length - 1)
        // console.log("ss",company)

        grantRoleDataRight({
          roleId: this.form.roleId,
          companyIds: company,
        }).then((res) => {
          const { msg, data, code } = res
          console.log('🚀 ~ data:', data)
          // 流程校验
          if (code == 1) {
            if (this.requireValuedata) {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'role',
                operationType: 12,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else {
              if (res.code == 1) {
                this.$message.success('授权成功')
                this.getRightInfo()
              }
            }
          }
        })
      },
      handleNodeClick(data) {
        console.log(data)
      },
      handleCheckChange(data, checked, indeterminate) {
        console.log(data, checked, indeterminate)
      },
      showEdit(row) {
        this.role = row
        this.form.roleId = row.rid
        console.log('角色------------------------')
        console.log(this.role)
        this.dialogFormVisible = true
        this.fetchData(1)
        this.fetchData(2)
      },
      // 注意：半选状态后端的checked为true，这里需要筛选出最里层子级里checked为true的item设置为默认选中值
      getLeafCheckedKeys(data, type) {
        let arr = []
        const acc = (data) => {
          data.forEach((i) => {
            console.log(i)
            if (i.children && i.children.length) {
              acc(i.children)
            } else {
              console.log(i.checked)
              if (i.checked) {
                if (type === 0) {
                  arr = arr.concat(i.id)
                } else {
                  arr = arr.concat(i.rightId)
                }
              }
            }
          })
          console.log('----------')
          console.log(arr)
          return arr
        }
        return acc(data)
      },
      getAllKeys(data, key) {
        let arr = []
        data.forEach((i) => {
          if (i[key]) arr.push(i.id)
          if (i.children && i.children.length) {
            arr = arr.concat(this.getAllKeys(i.children, key))
          }
        })
        return arr
      },
      fetchData(index) {
        if (index === 1) {
          this.loading = true
          getRoleRightList({
            roleId: this.form.roleId,
            moduleType: this.moduletype,
          })
            .then((res) => {
              this.data = res.data.rightList
              this.checkedKeys = this.getLeafCheckedKeys(this.data, 0)
              this.expandedKeys = this.getAllKeys(this.data, 'open')
            })
            .finally(() => {
              this.loading = false
            })
        }
        if (index === 2) {
          this.loading2 = true
          // getRoleRightListScreen({ roleId: this.form.roleId })
          //   .then((res) => {
          //     this.data2 = res.data.rightList
          //     this.checkedKeys2 = this.getLeafCheckedKeys(this.data2, 1)
          //     this.expandedKeys2 = this.getAllKeys(this.data2, 'open')
          //   })
          //   .finally(() => {
          //     this.loading2 = false
          //   })
          this.getRightInfo()
          this.loading2 = false
        }
      },
      close() {
        this.form = this.$options.data().form
        this.$emit('fetch-data')
        this.dialogFormVisible = false
      },
      moduleList() {
        getModuleList({}).then((res) => {
          this.moduleLists = res.data
        })
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
