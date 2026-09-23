<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
      :append-to-body="true"
      style="z-index: 9999; height: 100%"
    >
      <div class="right-wrapper">
        <el-select
          v-model="moduleType"
          placeholder="请选择模块"
          @change="handleModuleChange"
          @focus="saveCurrentModuleState"
        >
          <el-option
            v-for="item in moduleLists"
            :key="item.id"
            :label="item.projectName"
            :value="item.uniqueIdentification"
          />
        </el-select>
        <el-button native-type="submit" type="primary" @click="handleSave()">
          确定
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
    </el-dialog>
  </div>
</template>
<script>
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
  export default {
    name: 'MenuModal',
    data() {
      return {
        dialogFormVisible: false,
        data: [],
        checkedKeys: [],
        expandedKeys: [],
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        loading: false,
        moduleType: '',
        moduleLists: [],
        form: {
          roleId: '',
          rightIds: '',
          moduleType: 'xtsz',
        },
        // 存储每个模块的选中状态
        moduleStates: {},
        // 存储每个模块的展开状态
        moduleExpandedStates: {},
        // 存储每个模块的菜单名称
        moduleMenuNames: {},
      }
    },
    methods: {
      showEdit(roleId, rightIds, moduleType) {
        console.log('roleId', roleId)
        console.log('rightIds', rightIds)
        console.log('moduleType', moduleType)
        this.moduleList()
        this.dialogFormVisible = true
        this.form.roleId = roleId
        this.form.rightIds = rightIds

        this.form.moduleType = moduleType
        this.moduleType = moduleType
        this.fetchData(1)
      },
      close() {
        this.dialogFormVisible = false
      },
      saveCurrentModuleState() {
        // 在用户点击下拉框时保存当前模块状态
        if (this.$refs.tree && this.moduleType) {
          const halfChecked = this.$refs.tree.getHalfCheckedKeys()
          const checked = this.$refs.tree.getCheckedKeys()
          const allCheckedIds = halfChecked.concat(checked)

          // 保存当前模块的选中状态
          this.moduleStates[this.moduleType] = [...allCheckedIds]

          // 保存当前模块的展开状态
          this.moduleExpandedStates[this.moduleType] = [...this.expandedKeys]

          // 保存当前模块的菜单名称
          const currentModuleNames = this.getSelectedNodeNames(
            this.data,
            allCheckedIds
          )
          this.moduleMenuNames[this.moduleType] = currentModuleNames

          console.log(
            '保存模块状态:',
            this.moduleType,
            this.moduleStates[this.moduleType]
          )
          console.log(
            '保存模块菜单名称:',
            this.moduleType,
            this.moduleMenuNames[this.moduleType]
          )
        }
      },
      handleModuleChange(newModuleType) {
        // 切换到新模块
        this.fetchData(1)
      },
      fetchData(index) {
        if (index === 1) {
          this.loading = true
          getRoleRightList({
            roleId: this.form.roleId || '',
            moduleType: this.moduleType,
          })
            .then((res) => {
              this.data = res.data.rightList

              // 检查是否有该模块的保存状态
              const savedState = this.moduleStates[this.moduleType]
              const savedExpandedState =
                this.moduleExpandedStates[this.moduleType]

              console.log(
                '加载模块:',
                this.moduleType,
                '保存的状态:',
                savedState
              )

              if (savedState && savedState.length > 0) {
                // 如果有保存的状态，使用保存的状态
                this.checkedKeys = savedState
                this.expandedKeys =
                  savedExpandedState ||
                  this.getExpandedKeysForCheckedNodes(
                    this.data,
                    this.checkedKeys
                  )
                console.log('使用保存的状态回填')
              } else if (this.form.rightIds) {
                // 如果有传入的rightIds，则使用传入的rightIds进行回填
                this.checkedKeys = this.form.rightIds
                  .split(',')
                  .map((id) => parseInt(id))
                // 展开包含选中节点的父节点
                this.expandedKeys = this.getExpandedKeysForCheckedNodes(
                  this.data,
                  this.checkedKeys
                )
              } else {
                this.checkedKeys = this.getLeafCheckedKeys(this.data, 0)
                this.expandedKeys = this.getAllKeys(this.data, 'open')
              }
            })
            .finally(() => {
              this.loading = false
            })
        }
      },
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
      // 获取包含选中节点的父节点ID，用于展开树结构
      getExpandedKeysForCheckedNodes(data, checkedKeys) {
        let expandedKeys = []

        const findParentKeys = (nodes, targetIds, parentKeys = []) => {
          nodes.forEach((node) => {
            const currentPath = [...parentKeys, node.id]

            // 如果当前节点是目标节点之一，则其所有父节点都需要展开
            if (targetIds.includes(node.id)) {
              expandedKeys = expandedKeys.concat(parentKeys)
            }

            // 递归处理子节点
            if (node.children && node.children.length > 0) {
              const childExpandedKeys = findParentKeys(
                node.children,
                targetIds,
                currentPath
              )
              expandedKeys = expandedKeys.concat(childExpandedKeys)
            }
          })
          return expandedKeys
        }

        findParentKeys(data, checkedKeys)
        return [...new Set(expandedKeys)] // 去重
      },
      moduleList() {
        getModuleList({}).then((res) => {
          this.moduleLists = res.data
        })
      },
      handleCheckChange(data, checked, indeterminate) {
        // 获取当前选中的节点
        const halfChecked = this.$refs.tree.getHalfCheckedKeys()
        const checkedKeys = this.$refs.tree.getCheckedKeys()
        const allCheckedIds = halfChecked.concat(checkedKeys)

        // 保存当前模块的选中状态
        this.moduleStates[this.moduleType] = [...allCheckedIds]

        // 保存当前模块的展开状态
        this.moduleExpandedStates[this.moduleType] = [...this.expandedKeys]

        // 获取当前模块的选中菜单名称
        const currentModuleNames = this.getSelectedNodeNames(
          this.data,
          allCheckedIds
        )
        this.moduleMenuNames[this.moduleType] = currentModuleNames

        // 更新表单数据
        this.form.rightIds = allCheckedIds.join(',')

        // 获取所有模块的选中菜单名称
        const allSelectedNames = this.getAllModuleSelectedNames()

        // 向父组件发送数据变化事件
        this.$emit('menu-selection-change', {
          rightIds: this.form.rightIds,
          rightNames: allSelectedNames.join(','),
          moduleType: this.moduleType,
        })
      },
      getSelectedNodeNames(data, checkedIds) {
        let selectedNames = []

        const findNodeNames = (nodes) => {
          nodes.forEach((node) => {
            if (checkedIds.includes(node.id)) {
              selectedNames.push(node.name)
            }
            if (node.children && node.children.length > 0) {
              findNodeNames(node.children)
            }
          })
        }

        findNodeNames(data)
        return selectedNames
      },
      getAllModuleSelectedNames() {
        // 获取所有模块的选中菜单名称
        let allSelectedNames = []

        // 遍历所有保存的模块菜单名称
        Object.keys(this.moduleMenuNames).forEach((moduleType) => {
          const menuNames = this.moduleMenuNames[moduleType]
          if (menuNames && menuNames.length > 0) {
            allSelectedNames = allSelectedNames.concat(menuNames)
          }
        })

        return allSelectedNames
      },
      handleSave() {
        const halfChecked = this.$refs.tree.getHalfCheckedKeys()
        const checked = this.$refs.tree.getCheckedKeys()
        const allCheckedIds = halfChecked.concat(checked)

        // 保存当前模块的选中状态
        this.moduleStates[this.moduleType] = [...allCheckedIds]

        // 保存当前模块的展开状态
        this.moduleExpandedStates[this.moduleType] = [...this.expandedKeys]

        // 获取当前模块的选中菜单名称
        const currentModuleNames = this.getSelectedNodeNames(
          this.data,
          allCheckedIds
        )
        this.moduleMenuNames[this.moduleType] = currentModuleNames

        this.form.rightIds = allCheckedIds.join(',')
        this.form.moduleType = this.moduleType

        // 获取所有模块的选中菜单名称
        const allSelectedNames = this.getAllModuleSelectedNames()

        // 向父组件发送数据变化事件，只回填数据，不执行保存
        this.$emit('menu-selection-change', {
          rightIds: this.form.rightIds,
          rightNames: allSelectedNames.join(','),
          moduleType: this.moduleType,
        })

        // 关闭对话框
        this.close()
      },
    },
  }
</script>
