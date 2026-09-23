<template>
  <el-dialog
    title="📋 业务蓝图"
    :visible.sync="visible"
    width="95%"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="blueprint-dialog"
    @close="handleClose"
  >
    <div class="blueprint-content">
      <!-- 项目信息区域 -->
      <table class="blueprint-table">
        <tbody>
          <tr>
            <td class="section-label" rowspan="3">项目信息</td>
            <td class="field-label">客户名称：</td>
            <td colspan="3">
              <el-input v-model="form.customerName" size="small" placeholder="请输入客户名称" />
            </td>
          </tr>
          <tr>
            <td class="field-label">项目名称：</td>
            <td>
              <el-input v-model="form.projectName" size="small" placeholder="请输入项目名称" />
            </td>
            <td class="field-label">合同编号：</td>
            <td>
              <el-input v-model="form.contractNo" size="small" placeholder="请输入合同编号" />
            </td>
          </tr>
          <tr>
            <td class="field-label">提出单位：</td>
            <td>
              <el-input v-model="form.department" size="small" placeholder="请输入提出单位" />
            </td>
            <td class="field-label">提 出 人：</td>
            <td>
              <el-input v-model="form.proposer" size="small" placeholder="请输入提出人" />
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 需求内容区域 -->
      <div class="requirement-section">
        <div class="requirement-header">
          <span class="requirement-title">需求内容</span>
          <div class="requirement-header-actions">
            <el-button type="success" size="small" icon="el-icon-s-promotion" @click="sendSelected" :disabled="selectedRowIndexes.length === 0">
              发送选中({{ selectedRowIndexes.length }})
            </el-button>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addRequirement">新增</el-button>
          </div>
        </div>
        <div class="requirement-table-wrapper">
          <table class="requirement-table">
            <thead>
              <tr>
                <th class="col-check">
                  <el-checkbox v-model="selectAll" @change="handleSelectAll"></el-checkbox>
                </th>
                <th class="col-index">序号</th>
                <th class="col-status">问题状态</th>
                <th class="col-submitter">提交人</th>
                <th class="col-date">提交时间</th>
                <th class="col-domain">问题领域</th>
                <th class="col-module">问题模块</th>
                <th class="col-desc">问题描述</th>
                <th class="col-img">问题截图</th>
                <th class="col-level">紧急程度</th>
                <th class="col-resolve-date">预计解决时间</th>
                <th class="col-handler">处理人</th>
                <th class="col-solution">解决方案</th>
                <th class="col-resolved">是否解决</th>
                <th class="col-action">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in form.requirements" :key="index">
                <td class="col-check">
                  <el-checkbox v-model="item.selected"></el-checkbox>
                </td>
                <td class="col-index">{{ index + 1 }}</td>
                <td class="col-status">
                  <el-select v-model="item.status" size="mini" placeholder="请选择">
                    <el-option label="已完成" value="已完成" />
                    <el-option label="待处理" value="待处理" />
                  </el-select>
                </td>
                <td class="col-submitter">
                  <el-input v-model="item.submitter" size="mini" placeholder="提交人" />
                </td>
                <td class="col-date">
                  <el-date-picker v-model="item.submitTime" type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 130px;" />
                </td>
                <td class="col-domain">
                  <el-select v-model="item.domain" size="mini" placeholder="请选择">
                    <el-option label="需求调整" value="需求调整" />
                    <el-option label="新增需求" value="新增需求" />
                    <el-option label="BUG修复" value="BUG修复" />
                    <el-option label="功能优化" value="功能优化" />
                  </el-select>
                </td>
                <td class="col-module">
                  <div class="module-cell">
                    <span v-if="item.module" class="module-name-text">{{ item.module }}</span>
                    <el-button size="mini" type="text" icon="el-icon-s-operation" @click="openPageSelector(index)">
                      {{ item.module ? '重选' : '选择' }}
                    </el-button>
                  </div>
                </td>
                <td class="col-desc">
                  <el-input v-model="item.description" type="textarea" :rows="2" size="mini" placeholder="描述问题" />
                </td>
                <td class="col-img">
                  <el-upload
                    :action="''"
                    :auto-upload="false"
                    :on-change="(file) => handleImageChange(file, index)"
                    :file-list="item.imageList"
                    list-type="picture"
                    :limit="3"
                    accept="image/*"
                  >
                    <el-button size="mini" type="text" icon="el-icon-upload2">上传</el-button>
                  </el-upload>
                </td>
                <td class="col-level">
                  <el-select v-model="item.urgency" size="mini" placeholder="请选择">
                    <el-option label="高" value="高" />
                    <el-option label="中" value="中" />
                    <el-option label="低" value="低" />
                  </el-select>
                </td>
                <td class="col-resolve-date">
                  <el-date-picker v-model="item.resolveDate" type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 130px;" />
                </td>
                <td class="col-handler">
                  <el-input v-model="item.handler" size="mini" placeholder="处理人" />
                </td>
                <td class="col-solution">
                  <el-input v-model="item.solution" type="textarea" :rows="2" size="mini" placeholder="解决方案" />
                </td>
                <td class="col-resolved">
                  <el-select v-model="item.resolved" size="mini" placeholder="请选择">
                    <el-option label="是" value="是" />
                    <el-option label="否" value="否" />
                  </el-select>
                </td>
                <td class="col-action">
                  <el-button type="text" size="mini" icon="el-icon-delete" @click="removeRequirement(index)" class="delete-row-btn">删除</el-button>
                </td>
              </tr>
              <tr v-if="form.requirements.length === 0">
                <td colspan="15" class="empty-row">
                  <i class="el-icon-folder-opened"></i>
                  暂无数据，请点击右上角"新增"按钮添加需求
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Bug 列表区域 -->
      <div class="requirement-section bug-section">
        <div class="requirement-header">
          <span class="requirement-title">Bug 列表</span>
          <div class="requirement-header-actions">
            <el-button type="success" size="small" icon="el-icon-s-promotion" @click="sendSelectedBugs" :disabled="selectedBugIndexes.length === 0">
              发送选中({{ selectedBugIndexes.length }})
            </el-button>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addBug">新增 Bug</el-button>
          </div>
        </div>
        <div class="requirement-table-wrapper">
          <table class="requirement-table">
            <thead>
              <tr>
                <th class="col-check">
                  <el-checkbox v-model="selectAllBugs" @change="handleSelectAllBugs"></el-checkbox>
                </th>
                <th class="col-index">序号</th>
                <th class="col-status">状态</th>
                <th class="col-submitter">提交人</th>
                <th class="col-date">提交时间</th>
                <th class="col-module">所属模块</th>
                <th class="col-desc">问题描述</th>
                <th class="col-img">问题截图</th>
                <th class="col-level">紧急程度</th>
                <th class="col-resolve-date">预计解决时间</th>
                <th class="col-handler">处理人</th>
                <th class="col-solution">解决方案</th>
                <th class="col-resolved">是否解决</th>
                <th class="col-action">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(bug, bIndex) in form.bugs" :key="'bug-' + bIndex">
                <td class="col-check">
                  <el-checkbox v-model="bug.selected"></el-checkbox>
                </td>
                <td class="col-index">{{ bIndex + 1 }}</td>
                <td class="col-status">
                  <el-select v-model="bug.status" size="mini" placeholder="请选择">
                    <el-option label="已完成" value="已完成" />
                    <el-option label="待处理" value="待处理" />
                  </el-select>
                </td>
                <td class="col-submitter">
                  <el-input v-model="bug.submitter" size="mini" placeholder="提交人" />
                </td>
                <td class="col-date">
                  <el-date-picker v-model="bug.submitTime" type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 130px;" />
                </td>
                <td class="col-module">
                  <div class="module-cell">
                    <span v-if="bug.module" class="module-name-text">{{ bug.module }}</span>
                    <el-button size="mini" type="text" icon="el-icon-s-operation" @click="openBugPageSelector(bIndex)">
                      {{ bug.module ? '重选' : '选择' }}
                    </el-button>
                  </div>
                </td>
                <td class="col-desc">
                  <el-input v-model="bug.description" type="textarea" :rows="2" size="mini" placeholder="描述问题" />
                </td>
                <td class="col-img">
                  <el-upload
                    :action="''"
                    :auto-upload="false"
                    :on-change="(file) => handleBugImageChange(file, bIndex)"
                    :file-list="bug.imageList"
                    list-type="picture"
                    :limit="3"
                    accept="image/*"
                  >
                    <el-button size="mini" type="text" icon="el-icon-upload2">上传</el-button>
                  </el-upload>
                </td>
                <td class="col-level">
                  <el-select v-model="bug.urgency" size="mini" placeholder="请选择">
                    <el-option label="高" value="高" />
                    <el-option label="中" value="中" />
                    <el-option label="低" value="低" />
                  </el-select>
                </td>
                <td class="col-resolve-date">
                  <el-date-picker v-model="bug.resolveDate" type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 130px;" />
                </td>
                <td class="col-handler">
                  <el-input v-model="bug.handler" size="mini" placeholder="处理人" />
                </td>
                <td class="col-solution">
                  <el-input v-model="bug.solution" type="textarea" :rows="2" size="mini" placeholder="解决方案" />
                </td>
                <td class="col-resolved">
                  <el-select v-model="bug.resolved" size="mini" placeholder="请选择">
                    <el-option label="是" value="是" />
                    <el-option label="否" value="否" />
                  </el-select>
                </td>
                <td class="col-action">
                  <el-button type="text" size="mini" icon="el-icon-delete" @click="removeBug(bIndex)" class="delete-row-btn">删除</el-button>
                </td>
              </tr>
              <tr v-if="form.bugs.length === 0">
                <td colspan="14" class="empty-row">
                  <i class="el-icon-folder-opened"></i>
                  暂无 Bug，请点击右上角"新增 Bug"按钮添加
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 方案内容区域 -->
      <table class="blueprint-table" style="margin-top: 16px;">
        <tbody>
          <tr>
            <td class="section-label">方案内容</td>
            <td class="field-label">实施方案：</td>
            <td colspan="3">
              <el-input v-model="form.plan" type="textarea" :rows="4" size="small" placeholder="请详细描述实施方案内容" />
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 预估人天 -->
      <table class="blueprint-table">
        <tbody>
          <tr>
            <td class="section-label">预估人天</td>
            <td class="field-label">预估工时：</td>
            <td>
              <el-input-number v-model="form.estimatedDays" :min="1" :max="999" size="small" /> 人天
            </td>
            <td class="field-label">优先级：</td>
            <td>
              <el-select v-model="form.priority" size="small" placeholder="请选择">
                <el-option label="紧急" value="紧急" />
                <el-option label="高" value="高" />
                <el-option label="中" value="中" />
                <el-option label="低" value="低" />
              </el-select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button icon="el-icon-document" @click="openBlueprintList">蓝图列表</el-button>
      <el-button icon="el-icon-files" @click="openDraftDrawer">草稿箱</el-button>
      <el-button icon="el-icon-document-checked" :loading="savingDraft" @click="handleSaveDraft">保存草稿</el-button>
      <el-button type="success" icon="el-icon-folder-checked" :loading="savingBlueprint" @click="handleSaveBlueprint" :disabled="!canSubmit">
        保存蓝图
      </el-button>
      <el-button type="primary" @click="handleConfirm" :disabled="!canSubmit">
        <i class="el-icon-s-promotion"></i>
        发送到 AI
      </el-button>
    </div>

    <!-- 已保存蓝图列表抽屉 -->
    <el-drawer
      title="已保存蓝图"
      :visible.sync="blueprintListVisible"
      direction="rtl"
      size="380px"
      :append-to-body="true"
    >
      <div class="draft-drawer-body" v-loading="blueprintListLoading">
        <div v-if="blueprintList.length === 0 && !blueprintListLoading" class="draft-empty">
          <i class="el-icon-folder-opened"></i>
          <p>暂无已保存蓝图</p>
        </div>
        <div
          v-for="item in blueprintList"
          :key="item.id"
          class="draft-item"
        >
          <div class="draft-item-info" @click="loadBlueprint(item.id)">
            <div class="draft-item-title">{{ item.projectName || item.customerName || '未命名蓝图' }}</div>
            <div class="draft-item-time">{{ formatDraftTime(item.updateTime || item.createTime) }}</div>
          </div>
        </div>
      </div>
    </el-drawer>

    <!-- 草稿箱抽屉 -->
    <el-drawer
      title="业务蓝图草稿箱"
      :visible.sync="draftDrawerVisible"
      direction="rtl"
      size="380px"
      :append-to-body="true"
    >
      <div class="draft-drawer-body" v-loading="draftListLoading">
        <div v-if="draftList.length === 0 && !draftListLoading" class="draft-empty">
          <i class="el-icon-folder-opened"></i>
          <p>暂无草稿</p>
        </div>
        <div
          v-for="item in draftList"
          :key="item.id"
          class="draft-item"
        >
          <div class="draft-item-info" @click="loadDraft(item.id)">
            <div class="draft-item-title">{{ item.title || '未命名草稿' }}</div>
            <div class="draft-item-time">{{ formatDraftTime(item.updateTime || item.createTime) }}</div>
          </div>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            class="draft-item-del"
            @click.stop="handleDeleteDraft(item)"
          ></el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 页面选择器弹窗 -->
    <el-dialog
      title="选择页面"
      :visible.sync="pageSelectorVisible"
      width="500px"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      custom-class="page-selector-dialog"
    >
      <div class="page-selector-content">
        <el-select
          v-model="pageSelectedModule"
          placeholder="请选择模块"
          style="width: 100%; margin-bottom: 16px; text-align: left;"
          @change="fetchPageList"
          popper-append-to-body
        >
          <el-option
            v-for="item in moduleList"
            :key="item.uniqueIdentification"
            :label="item.projectName"
            :value="item.uniqueIdentification"
          />
        </el-select>

        <el-input
          v-model="pageSearchKey"
          placeholder="搜索页面名称"
          prefix-icon="el-icon-search"
          style="margin-bottom: 12px;"
          clearable
          @input="filterPageList"
        />

        <div class="page-tree" v-loading="pageListLoading">
          <el-tree
            ref="blueprintPageTree"
            :data="filteredPageList"
            :props="treeProps"
            node-key="id"
            :expand-on-click-node="false"
            :default-expand-all="true"
            highlight-current
            @node-click="handlePageNodeClick"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <div class="tree-node-content">
                <span :class="{ 'disabled-node': data.type === 0 }">{{ node.label }}</span>
                <span v-if="data.type === 0" class="type-label">(目录)</span>
              </div>
            </span>
          </el-tree>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="pageSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPageSelect" :disabled="!tempSelectedPage">确定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getAuthList } from '@/api/setting/auths'
import { getModuleList } from '@/api/setting/system'
import { saveBlueprintDraft, getBlueprintDraftList, getBlueprintDraftDetail, deleteBlueprintDraft } from '@/api/ai/blueprintDraft'
import { saveBlueprint, getBlueprintList, getBlueprintDetail } from '@/api/ai/blueprint'

export default {
  name: 'BlueprintDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    selectedPages: {
      type: Array,
      default: () => []
    },
    // 从业务梳理传入的预填需求条目
    initialRequirements: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      form: {
        customerName: '中国船舶集团投资有限公司',
        projectName: '客户A投资信息化建设项目',
        contractNo: '',
        department: '',
        proposer: '',
        requirements: [],
        bugs: [],
        plan: '',
        estimatedDays: 5,
        priority: '中'
      },
      // 结构化保存
      savingBlueprint: false,
      // 页面选择器
      pageSelectorVisible: false,
      pageListLoading: false,
      pageSearchKey: '',
      pageList: [],
      filteredPageList: [],
      moduleList: [],
      pageSelectedModule: 'xtsz',
      treeProps: { children: 'children', label: 'name' },
      tempSelectedPage: null,
      currentEditingRowIndex: -1,
      currentEditingTarget: 'requirement',   // requirement | bug
      selectAll: false,
      selectAllBugs: false,
      // 蓝图列表
      blueprintListVisible: false,
      blueprintListLoading: false,
      blueprintList: [],
      // 草稿箱
      draftDrawerVisible: false,
      draftListLoading: false,
      draftList: [],
      savingDraft: false
    }
  },
  computed: {
    canSubmit() {
      return this.form.requirements.length > 0 || this.form.bugs.length > 0 || this.form.plan.trim()
    },
    selectedRowIndexes() {
      return this.form.requirements
        .map((item, index) => item.selected ? index : -1)
        .filter(i => i !== -1)
    },
    selectedBugIndexes() {
      return this.form.bugs
        .map((item, index) => item.selected ? index : -1)
        .filter(i => i !== -1)
    },
    userId() {
      try {
        const raw = localStorage.getItem('userInfo')
        const info = raw ? JSON.parse(raw) : {}
        return info.staffid || info.staffId || info.id || 'anonymous'
      } catch { return 'anonymous' }
    },
    userName() {
      try {
        const raw = localStorage.getItem('userInfo')
        const info = raw ? JSON.parse(raw) : {}
        return info.staffname || info.staffName || info.name || ''
      } catch { return '' }
    }
  },
  created() {
    this.fetchModuleList()
  },
  watch: {
    // 每次打开时，若有预填需求则自动填充
    visible(val) {
      if (val && this.initialRequirements && this.initialRequirements.length > 0) {
        this.form.requirements = this.initialRequirements.map(r => ({ ...r, selected: false }))
      }
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
    },
    handleConfirm() {
      const content = this.buildContent()
      this.$emit('confirm', content)
      this.handleClose()
    },
    /** 新增一行需求 */
    addRequirement() {
      this.form.requirements.push({
        selected: false,
        status: '待处理',
        submitter: '',
        submitTime: '',
        domain: '',
        module: '',
        description: '',
        imageList: [],
        urgency: '中',
        resolveDate: '',
        handler: '',
        solution: '',
        resolved: '否'
      })
    },
    /** 删除一行需求 */
    removeRequirement(index) {
      this.form.requirements.splice(index, 1)
    },
    /** 新增一行 Bug */
    addBug() {
      this.form.bugs.push({
        selected: false,
        status: '待处理',
        submitter: '',
        submitTime: '',
        module: '',
        description: '',
        imageList: [],
        urgency: '中',
        resolveDate: '',
        handler: '',
        solution: '',
        resolved: '否'
      })
    },
    /** 删除一行 Bug */
    removeBug(index) {
      this.form.bugs.splice(index, 1)
    },
    /** 处理 Bug 图片选择 */
    handleBugImageChange(file, index) {
      if (!this.form.bugs[index].imageList) {
        this.$set(this.form.bugs[index], 'imageList', [])
      }
      this.form.bugs[index].imageList.push(file)
    },
    /** 打开 Bug 行的页面选择器（复用需求行选择器，标记为 bug 模式） */
    openBugPageSelector(rowIndex) {
      this.currentEditingRowIndex = rowIndex
      this.currentEditingTarget = 'bug'
      this.tempSelectedPage = null
      this.pageSearchKey = ''
      this.pageSelectorVisible = true
      this.fetchPageList()
    },
    /** 处理图片选择 */
    handleImageChange(file, index) {
      if (!this.form.requirements[index].imageList) {
        this.$set(this.form.requirements[index], 'imageList', [])
      }
      this.form.requirements[index].imageList.push(file)
    },
    /** 全选/取消全选 */
    handleSelectAll(val) {
      this.form.requirements.forEach(item => {
        this.$set(item, 'selected', val)
      })
    },
    /** Bug 全选/取消全选 */
    handleSelectAllBugs(val) {
      this.form.bugs.forEach(item => {
        this.$set(item, 'selected', val)
      })
    },
    /** 发送选中的 Bug 到 AI */
    sendSelectedBugs() {
      const selectedItems = this.form.bugs.filter(item => item.selected)
      if (selectedItems.length === 0) {
        this.$message.warning('请至少选择一条 Bug')
        return
      }
      let content = `## 业务蓝图\n\n`
      content += `### 项目信息\n`
      content += `- 客户名称：${this.form.customerName}\n`
      content += `- 项目名称：${this.form.projectName}\n`
      if (this.form.contractNo) content += `- 合同编号：${this.form.contractNo}\n`
      if (this.form.department) content += `- 提出单位：${this.form.department}\n`
      if (this.form.proposer) content += `- 提出人：${this.form.proposer}\n`
      content += `\n### Bug 列表（已选${selectedItems.length}条）\n\n`
      content += `| 序号 | 状态 | 提交人 | 提交时间 | 所属模块 | 问题描述 | 紧急程度 | 预计解决时间 | 处理人 | 解决方案 | 是否解决 |\n`
      content += `| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |\n`
      selectedItems.forEach((bug, idx) => {
        content += `| ${idx + 1} | ${bug.status} | ${bug.submitter} | ${bug.submitTime} | ${bug.module} | ${bug.description} | ${bug.urgency} | ${bug.resolveDate} | ${bug.handler} | ${bug.solution} | ${bug.resolved} |\n`
      })
      if (this.form.plan) {
        content += `\n### 实施方案\n${this.form.plan}\n`
      }
      content += `\n- 预估人天：${this.form.estimatedDays} 人天\n`
      content += `- 优先级：${this.form.priority}\n`
      content += `\n---\n请根据以上业务蓝图选中的 Bug 列表，进行详细的技术方案设计和开发实施。`
      this.$emit('confirm', content)
      this.handleClose()
    },
    /** 发送选中的需求到AI */
    sendSelected() {
      const selectedItems = this.form.requirements.filter(item => item.selected)
      if (selectedItems.length === 0) {
        this.$message.warning('请至少选择一条需求')
        return
      }
      const content = this.buildContentFromItems(selectedItems)
      this.$emit('confirm', content)
      this.handleClose()
    },
    /** 根据指定条目组装内容 */
    buildContentFromItems(items) {
      let content = `## 业务蓝图\n\n`
      content += `### 项目信息\n`
      content += `- 客户名称：${this.form.customerName}\n`
      content += `- 项目名称：${this.form.projectName}\n`
      if (this.form.contractNo) content += `- 合同编号：${this.form.contractNo}\n`
      if (this.form.department) content += `- 提出单位：${this.form.department}\n`
      if (this.form.proposer) content += `- 提出人：${this.form.proposer}\n`

      content += `\n### 需求内容（已选${items.length}条）\n\n`
      content += `| 序号 | 问题状态 | 提交人 | 提交时间 | 问题领域 | 问题模块 | 问题描述 | 紧急程度 | 预计解决时间 | 处理人 | 解决方案 | 是否解决 |\n`
      content += `| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |\n`
      items.forEach((req, idx) => {
        content += `| ${idx + 1} | ${req.status} | ${req.submitter} | ${req.submitTime} | ${req.domain} | ${req.module} | ${req.description} | ${req.urgency} | ${req.resolveDate} | ${req.handler} | ${req.solution} | ${req.resolved} |\n`
      })

      if (this.form.plan) {
        content += `\n### 实施方案\n${this.form.plan}\n`
      }
      content += `\n- 预估人天：${this.form.estimatedDays} 人天\n`
      content += `- 优先级：${this.form.priority}\n`
      content += `\n---\n请根据以上业务蓝图选中的需求内容，进行详细的技术方案设计和开发实施。`
      return content
    },

    // ==================== 页面选择器 ====================
    /** 打开页面选择器 */
    openPageSelector(rowIndex) {
      this.currentEditingRowIndex = rowIndex
      this.currentEditingTarget = 'requirement'
      this.tempSelectedPage = null
      this.pageSearchKey = ''
      this.pageSelectorVisible = true
      this.fetchPageList()
    },
    /** 获取模块列表 */
    async fetchModuleList() {
      try {
        const res = await getModuleList({})
        this.moduleList = res.data || []
      } catch (e) {
        console.error('[BlueprintDialog] 获取模块列表失败:', e)
      }
    },
    /** 获取页面列表 */
    async fetchPageList() {
      if (!this.pageSelectedModule) return
      this.pageListLoading = true
      try {
        const { data } = await getAuthList({
          moduletype: this.pageSelectedModule,
          judge: 1
        })
        this.pageList = this.buildPageTree(data.rightList || [])
        this.filterPageList()
      } catch (e) {
        console.error('[BlueprintDialog] 获取页面列表失败:', e)
      } finally {
        this.pageListLoading = false
      }
    },
    /** 构建页面树 */
    buildPageTree(nodes) {
      if (!nodes) return []
      return nodes
        .filter(node => node.type !== 2)
        .map(node => ({
          ...node,
          children: this.buildPageTree(node.children)
        }))
    },
    /** 过滤页面列表 */
    filterPageList() {
      if (!this.pageSearchKey) {
        this.filteredPageList = this.pageList
      } else {
        this.filteredPageList = this.filterTree(this.pageList, this.pageSearchKey)
      }
    },
    /** 树形过滤 */
    filterTree(tree, keyword) {
      if (!tree) return []
      const result = []
      for (const node of tree) {
        const match = node.name.toLowerCase().includes(keyword.toLowerCase())
        const filteredChildren = this.filterTree(node.children, keyword)
        if (match || filteredChildren.length > 0) {
          result.push({ ...node, children: filteredChildren })
        }
      }
      return result
    },
    /** 点击树节点 */
    handlePageNodeClick(data) {
      // 只允许选择页面节点（type === 1），不允许选目录
      if (data.type === 1) {
        this.tempSelectedPage = data
      } else {
        this.tempSelectedPage = null
      }
    },
    /** 确认页面选择 */
    confirmPageSelect() {
      if (!this.tempSelectedPage) return
      const idx = this.currentEditingRowIndex
      const list = this.currentEditingTarget === 'bug' ? this.form.bugs : this.form.requirements
      if (idx >= 0 && list[idx]) {
        this.$set(list[idx], 'module', this.tempSelectedPage.name)
      }
      this.pageSelectorVisible = false
    },

    /** 组装发送内容 */
    buildContent() {
      let content = `## 业务蓝图\n\n`
      content += `### 项目信息\n`
      content += `- 客户名称：${this.form.customerName}\n`
      content += `- 项目名称：${this.form.projectName}\n`
      if (this.form.contractNo) content += `- 合同编号：${this.form.contractNo}\n`
      if (this.form.department) content += `- 提出单位：${this.form.department}\n`
      if (this.form.proposer) content += `- 提出人：${this.form.proposer}\n`

      if (this.form.requirements.length > 0) {
        content += `\n### 需求内容（共${this.form.requirements.length}条）\n\n`
        content += `| 序号 | 问题状态 | 提交人 | 提交时间 | 问题领域 | 问题模块 | 问题描述 | 紧急程度 | 预计解决时间 | 处理人 | 解决方案 | 是否解决 |\n`
        content += `| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |\n`
        this.form.requirements.forEach((req, idx) => {
          content += `| ${idx + 1} | ${req.status} | ${req.submitter} | ${req.submitTime} | ${req.domain} | ${req.module} | ${req.description} | ${req.urgency} | ${req.resolveDate} | ${req.handler} | ${req.solution} | ${req.resolved} |\n`
        })
      }

      if (this.form.plan) {
        content += `\n### 实施方案\n${this.form.plan}\n`
      }
      content += `\n- 预估人天：${this.form.estimatedDays} 人天\n`
      content += `- 优先级：${this.form.priority}\n`
      content += `\n---\n请根据以上业务蓝图的内容，进行详细的技术方案设计和开发实施。`
      return content
    },

    // ==================== 草稿箱 ====================
    /** 保存草稿 */
    async handleSaveDraft() {
      if (this.savingDraft) return
      this.savingDraft = true
      try {
        // 序列化表单，图片转 base64 存入 formData
        const formSnapshot = JSON.parse(JSON.stringify(this.form))
        // 处理图片：将 File 对象转为 base64
        for (const req of formSnapshot.requirements) {
          if (req.imageList && req.imageList.length > 0) {
            req.imageList = await Promise.all(
              req.imageList.map(f => this.fileToBase64(f.raw || f))
            )
          }
        }
        for (const bug of (formSnapshot.bugs || [])) {
          if (bug.imageList && bug.imageList.length > 0) {
            bug.imageList = await Promise.all(
              bug.imageList.map(f => this.fileToBase64(f.raw || f))
            )
          }
        }
        const res = await saveBlueprintDraft({
          userId: this.userId,
          userName: this.userName,
          title: this.form.projectName || '未命名草稿',
          formData: JSON.stringify(formSnapshot)
        })
        if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
          this.$message.success('草稿已保存')
        } else {
          this.$message.error('保存失败：' + (res && res.message ? res.message : '未知错误'))
        }
      } catch (e) {
        console.error('[BlueprintDialog] 保存草稿失败:', e)
        this.$message.error('保存失败，请稍后重试')
      } finally {
        this.savingDraft = false
      }
    },
    /** 保存蓝图（结构化落库：主表 + 需求点 + Bug） */
    async handleSaveBlueprint() {
      if (this.savingBlueprint) return
      this.savingBlueprint = true
      try {
        const snapshot = JSON.parse(JSON.stringify(this.form))
        // 图片 File → base64，并把 imageList 序列化为 JSON 字符串入库
        for (const req of (snapshot.requirements || [])) {
          if (req.imageList && req.imageList.length > 0) {
            const imgs = await Promise.all(req.imageList.map(f => this.fileToBase64(f.raw || f)))
            req.imageList = JSON.stringify(imgs)
          } else {
            req.imageList = ''
          }
        }
        for (const bug of (snapshot.bugs || [])) {
          if (bug.imageList && bug.imageList.length > 0) {
            const imgs = await Promise.all(bug.imageList.map(f => this.fileToBase64(f.raw || f)))
            bug.imageList = JSON.stringify(imgs)
          } else {
            bug.imageList = ''
          }
        }
        const payload = {
          customerName: snapshot.customerName,
          projectName: snapshot.projectName,
          contractNo: snapshot.contractNo,
          department: snapshot.department,
          proposer: snapshot.proposer,
          plan: snapshot.plan,
          estimatedDays: snapshot.estimatedDays,
          priority: snapshot.priority,
          creatorId: this.userId,
          creatorName: this.userName,
          requirements: snapshot.requirements || [],
          bugs: snapshot.bugs || []
        }
        const res = await saveBlueprint(payload)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
          this.$message.success('蓝图已保存')
        } else {
          this.$message.error('保存失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        console.error('[BlueprintDialog] 保存蓝图失败:', e)
        this.$message.error('保存失败，请稍后重试')
      } finally {
        this.savingBlueprint = false
      }
    },
    /** File 转 base64 */
    fileToBase64(file) {
      if (!file || typeof file !== 'object' || !file.size) {
        // 已经是 base64 字符串或无效对象，直接返回
        return Promise.resolve(file)
      }
      return new Promise((resolve) => {
        const reader = new FileReader()
        reader.onload = e => resolve({ name: file.name, type: file.type, base64: e.target.result })
        reader.onerror = () => resolve(null)
        reader.readAsDataURL(file)
      })
    },
    /** 打开蓝图列表 */
    async openBlueprintList() {
      this.blueprintListVisible = true
      this.blueprintListLoading = true
      try {
        const res = await getBlueprintList(this.userId)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
          this.blueprintList = res.data || []
        } else {
          this.blueprintList = []
        }
      } catch (e) {
        console.error('[BlueprintDialog] 获取蓝图列表失败:', e)
        this.blueprintList = []
      } finally {
        this.blueprintListLoading = false
      }
    },
    /** 加载已保存蓝图详情并填充表单 */
    async loadBlueprint(id) {
      try {
        const res = await getBlueprintDetail(id)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0) && res.data) {
          const { blueprint, requirements, bugs } = res.data
          this.form = {
            ...this.form,
            customerName: blueprint.customerName || '',
            projectName: blueprint.projectName || '',
            contractNo: blueprint.contractNo || '',
            department: blueprint.department || '',
            proposer: blueprint.proposer || '',
            plan: blueprint.plan || '',
            estimatedDays: blueprint.estimatedDays || 5,
            priority: blueprint.priority || '中',
            requirements: (requirements || []).map(r => ({
              selected: false,
              status: r.status || '待处理',
              submitter: r.submitter || '',
              submitTime: r.submitTime || '',
              domain: r.domain || '',
              module: r.module || '',
              description: r.description || '',
              imageList: r.imageList ? JSON.parse(r.imageList) : [],
              urgency: r.urgency || '中',
              resolveDate: r.resolveDate || '',
              handler: r.handler || '',
              solution: r.solution || '',
              resolved: r.resolved || '否'
            })),
            bugs: (bugs || []).map(b => ({
              selected: false,
              status: b.status || '待处理',
              submitter: b.submitter || '',
              submitTime: b.submitTime || '',
              module: b.module || '',
              description: b.description || '',
              imageList: b.imageList ? JSON.parse(b.imageList) : [],
              urgency: b.urgency || '中',
              resolveDate: b.resolveDate || '',
              handler: b.handler || '',
              solution: b.solution || '',
              resolved: b.resolved || '否'
            }))
          }
          this.blueprintListVisible = false
          this.$message.success('蓝图已加载')
        } else {
          this.$message.error('加载蓝图失败')
        }
      } catch (e) {
        console.error('[BlueprintDialog] 加载蓝图失败:', e)
        this.$message.error('加载失败，请稍后重试')
      }
    },
    /** 打开草稿箱 */
    async openDraftDrawer() {
      this.draftDrawerVisible = true
      this.draftListLoading = true
      try {
        const res = await getBlueprintDraftList(this.userId)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
          this.draftList = res.data || []
        } else {
          this.draftList = []
        }
      } catch (e) {
        console.error('[BlueprintDialog] 获取草稿列表失败:', e)
        this.draftList = []
      } finally {
        this.draftListLoading = false
      }
    },
    /** 加载草稿详情并填充表单 */
    async loadDraft(id) {
      try {
        const res = await getBlueprintDraftDetail(id)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0) && res.data && res.data.formData) {
          const formData = JSON.parse(res.data.formData)
          this.form = { ...this.form, ...formData }
          this.draftDrawerVisible = false
          this.$message.success('草稿已加载')
        } else {
          this.$message.error('加载草稿失败')
        }
      } catch (e) {
        console.error('[BlueprintDialog] 加载草稿失败:', e)
        this.$message.error('加载失败，请稍后重试')
      }
    },
    /** 删除草稿 */
    async handleDeleteDraft(item) {
      try {
        await this.$confirm(`确定删除草稿「${item.title || '未命名草稿'}」吗？`, '删除确认', {
          confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
        })
      } catch { return }
      try {
        const res = await deleteBlueprintDraft(item.id)
        if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
          this.$message.success('已删除')
          this.draftList = this.draftList.filter(d => d.id !== item.id)
        } else {
          this.$message.error('删除失败')
        }
      } catch (e) {
        this.$message.error('删除失败，请稍后重试')
      }
    },
    /** 格式化草稿时间 */
    formatDraftTime(time) {
      if (!time) return ''
      const d = new Date(time)
      if (isNaN(d.getTime())) return String(time).slice(0, 16)
      const pad = n => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    }
  }
}
</script>

<style scoped>
/* ==================== 业务蓝图对话框样式 ==================== */
.blueprint-dialog {
  text-align: left !important;
}

.blueprint-dialog >>> .el-dialog__header {
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 100%);
  padding: 15px 20px;
  border-radius: 4px 4px 0 0;
}

.blueprint-dialog >>> .el-dialog__title {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}

.blueprint-dialog >>> .el-dialog__headerbtn .el-dialog__close {
  color: #fff;
}

.blueprint-content {
  padding: 0;
  max-height: 65vh;
  overflow-y: auto;
}

/* 表格样式 */
.blueprint-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #d0d7de;
  margin-bottom: 0;
  font-size: 14px;
}

.blueprint-table + .blueprint-table {
  border-top: none;
}

.blueprint-table td {
  padding: 10px 14px;
  border: 1px solid #d0d7de;
  vertical-align: middle;
}

.blueprint-table .section-label {
  width: 100px;
  text-align: center;
  font-weight: 700;
  color: #1a1a1a;
  background: #f0f3f6;
  font-size: 14px;
  letter-spacing: 2px;
}

.blueprint-table .field-label {
  width: 110px;
  font-weight: 600;
  color: #333;
  background: #f8f9fb;
  white-space: nowrap;
}

/* ==================== 需求内容区域 ==================== */
.requirement-section {
  margin-top: 16px;
  border: 1px solid #d0d7de;
  border-radius: 4px;
  overflow: hidden;
}

/* Bug 列表分区：用暖色调与需求清单区分 */
.bug-section .requirement-header {
  background: #fef3f2;
  border-bottom-color: #fcdcd6;
}
.bug-section .requirement-title {
  color: #c0392b;
}

.requirement-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #f0f3f6;
  border-bottom: 1px solid #d0d7de;
}

.requirement-header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.requirement-title {
  font-weight: 700;
  font-size: 14px;
  color: #1a1a1a;
  letter-spacing: 2px;
}

.requirement-table-wrapper {
  overflow-x: auto;
}

.requirement-table {
  width: 100%;
  min-width: 1400px;
  border-collapse: collapse;
  font-size: 13px;
}

.requirement-table thead tr {
  background: #f8f9fb;
}

.requirement-table th {
  padding: 10px 8px;
  border: 1px solid #d0d7de;
  font-weight: 600;
  color: #333;
  text-align: center;
  white-space: nowrap;
}

.requirement-table td {
  padding: 8px 6px;
  border: 1px solid #d0d7de;
  vertical-align: middle;
  text-align: center;
}

.requirement-table .col-check { width: 40px; }
.requirement-table .col-index { width: 45px; }
.requirement-table .col-status { width: 90px; }
.requirement-table .col-submitter { width: 80px; }
.requirement-table .col-date { width: 140px; }
.requirement-table .col-domain { width: 100px; }
.requirement-table .col-module { width: 120px; }
.requirement-table .col-desc { width: 180px; }
.requirement-table .col-img { width: 90px; }
.requirement-table .col-level { width: 90px; }
.requirement-table .col-resolve-date { width: 140px; }
.requirement-table .col-handler { width: 80px; }
.requirement-table .col-solution { width: 180px; }
.requirement-table .col-resolved { width: 80px; }
.requirement-table .col-action { width: 60px; }

.requirement-table tbody tr:hover {
  background: #f5f9ff;
}

.empty-row {
  text-align: center;
  color: #909399;
  padding: 30px 0 !important;
  font-size: 14px;
}

.empty-row i {
  margin-right: 6px;
}

.delete-row-btn {
  color: #F56C6C;
}

.delete-row-btn:hover {
  color: #f33;
}

/* 问题模块单元格 */
.module-cell {
  display: flex;
  align-items: center;
  gap: 4px;
}

.module-name-text {
  font-size: 12px;
  color: #409EFF;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Footer */
.blueprint-dialog >>> .dialog-footer {
  padding: 15px 20px;
  border-top: 1px solid #EBEEF5;
}

.blueprint-dialog >>> .dialog-footer .el-button--primary {
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 100%);
  border: none;
}

.blueprint-dialog >>> .dialog-footer .el-button--primary:hover {
  background: linear-gradient(135deg, #1565c0 0%, #0a3d8f 100%);
}

/* ==================== 草稿箱抽屉 ==================== */
.draft-drawer-body {
  padding: 12px 16px;
  min-height: 200px;
}

.draft-empty {
  text-align: center;
  color: #c0c4cc;
  padding: 40px 0;
  font-size: 14px;
}

.draft-empty i {
  font-size: 36px;
  display: block;
  margin-bottom: 10px;
}

.draft-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 10px;
  border-radius: 6px;
  margin-bottom: 8px;
  background: #f9f9f9;
  border: 1px solid #EBEEF5;
  transition: background 0.15s;
}

.draft-item:hover {
  background: #ECF5FF;
  border-color: rgba(64, 158, 255, 0.3);
}

.draft-item-info {
  flex: 1;
  cursor: pointer;
  min-width: 0;
}

.draft-item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}

.draft-item-time {
  font-size: 12px;
  color: #909399;
}

.draft-item-del {
  flex-shrink: 0;
  color: #c0c4cc;
  padding: 4px;
}

.draft-item-del:hover {
  color: #F56C6C !important;
}
</style>