<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    custom-class="business-review-dialog"
    width="92%"
    top="4vh"
    @opened="onOpened"
    @closed="onClosed"
  >
    <!-- 头部 -->
    <div slot="title" class="br-header">
      <div class="br-title">
        <i class="el-icon-collection-tag"></i>
        <span>业务梳理</span>
      </div>
      <div class="br-actions">
        <!-- 常驻核心操作 -->
        <el-tooltip content="将当前内容发布为新版本，所有用户默认看到最新版本" placement="bottom">
          <el-button size="small" type="primary" icon="el-icon-upload2" plain @click="openPublishDialog">发布新版本</el-button>
        </el-tooltip>
        <el-tooltip content="新建一条业务需求（可选择内置流程模板或自定义空白文档，左侧列表新增节点）" placement="bottom">
          <el-button size="small" type="success" icon="el-icon-plus" plain @click="openNewTplDialog">新建需求</el-button>
        </el-tooltip>
        <el-tooltip content="AI 分析全文内容，自动填充到业务蓝图需求表" placement="bottom">
          <el-button size="small" type="warning" icon="el-icon-s-data" plain :loading="sendingToBlueprint" @click="sendToBlueprint">
            发送至业务蓝图
          </el-button>
        </el-tooltip>
        <el-tooltip content="对当前需求文档最新版本签字确认，确认后方可发送至业务蓝图" placement="bottom">
          <el-button
            size="small"
            :type="currentDoc && currentDoc.confirmStatus === 1 ? 'success' : 'info'"
            :icon="currentDoc && currentDoc.confirmStatus === 1 ? 'el-icon-circle-check' : 'el-icon-edit-outline'"
            plain
            @click="openConfirmDialog"
          >
            {{ currentDoc && currentDoc.confirmStatus === 1 ? '已确认' : '签字确认' }}
          </el-button>
        </el-tooltip>
        <el-tooltip :content="readonly ? '当前为只读模式，点击切换为编辑' : '当前为编辑模式，点击切换为只读'" placement="bottom">
          <el-button size="small" :icon="readonly ? 'el-icon-lock' : 'el-icon-edit'" @click="toggleReadonly">
            {{ readonly ? '只读' : '编辑' }}
          </el-button>
        </el-tooltip>

        <!-- 更多操作下拉 -->
        <el-dropdown trigger="click" placement="bottom-end" @command="handleMoreCommand">
          <el-button size="small" icon="el-icon-more">
            更多<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="saveDraft" icon="el-icon-document-checked">
              保存草稿
            </el-dropdown-item>
            <el-dropdown-item command="openDraftDrawer" icon="el-icon-files">
              草稿箱
              <el-badge v-if="totalDraftCount > 0" :value="totalDraftCount" class="br-more-badge" />
            </el-dropdown-item>
            <el-dropdown-item command="openVersionDrawer" icon="el-icon-time">
              历史版本
              <span v-if="currentVersionNo" class="br-ver-tag">v{{ currentVersionNo }}</span>
            </el-dropdown-item>
            <el-dropdown-item command="exportWord" icon="el-icon-download" divided>
              导出 Word
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <el-button size="small" type="danger" icon="el-icon-close" plain @click="close">关闭</el-button>
      </div>
    </div>

    <!-- 三栏主体 -->
    <div class="br-body">
      <!-- ============ 左栏：文档列表 ============ -->
      <aside class="br-left">
        <div class="br-left-search">
          <el-input
            v-model="searchKey"
            size="small"
            placeholder="搜索流程文档"
            prefix-icon="el-icon-search"
            clearable
          />
        </div>
        <div class="br-left-list">
          <div
            v-for="item in filteredList"
            :key="item.id"
            class="br-doc-item"
            :class="{ active: currentId === item.id }"
            @click="selectDoc(item)"
          >
            <div class="br-doc-no">{{ item.no }}</div>
            <div class="br-doc-meta">
              <div class="br-doc-title" :title="item.title">{{ item.title }}</div>
              <div class="br-doc-sub">{{ item.subtitle || '内控流程' }}</div>
            </div>
            <i
              v-if="item.confirmStatus === 1"
              class="el-icon-circle-check br-doc-confirmed"
              title="已签字确认"
            ></i>
            <i v-if="hasDraft(item.id)" class="el-icon-edit-outline br-doc-mark" title="存在草稿"></i>
            <i
              v-if="item.isSystem !== 1"
              class="el-icon-delete br-doc-delete"
              title="删除模板"
              @click.stop="handleDeleteTemplate(item)"
            ></i>
          </div>
          <div v-if="filteredList.length === 0" class="br-empty">无匹配文档</div>
        </div>
      </aside>

      <!-- ============ 中栏：正文 + AI 对话 ============ -->
      <section class="br-center">
        <!-- 正文头条 -->
        <div class="br-doc-header">
          <div class="br-doc-h-left">
            <span class="br-doc-h-no">{{ currentDoc ? currentDoc.no : '--' }}</span>
            <span class="br-doc-h-title">{{ currentDoc ? currentDoc.title : '请选择文档' }}</span>
          </div>
          <div class="br-doc-h-right">
            <el-tooltip content="字体缩小" placement="top">
              <el-button size="mini" icon="el-icon-minus" circle @click="changeFontSize(-1)"></el-button>
            </el-tooltip>
            <span class="br-fontsize">{{ fontSize }}px</span>
            <el-tooltip content="字体放大" placement="top">
              <el-button size="mini" icon="el-icon-plus" circle @click="changeFontSize(1)"></el-button>
            </el-tooltip>
          </div>
        </div>

        <!-- 正文区（占 4/5） -->
        <div
          ref="docArea"
          class="br-doc-content no-copy"
          :class="{ readonly: readonly }"
          :contenteditable="!readonly"
          :style="{ fontSize: fontSize + 'px' }"
          v-html="docHtml"
          @copy.prevent="onForbidCopy"
          @cut.prevent="onForbidCopy"
          @contextmenu.prevent="onForbidCopy"
          @dragstart.prevent
          @drop.prevent
          @keydown="onKeydown"
          @input="onDocInput"
          @mouseup="onDocMouseUp"
        ></div>

        <!-- 划词快速提问浮窗 -->
        <AiInlineEdit ref="aiInlineEdit" />

        <!-- AI 对话框（占 1/5） -->
        <div class="br-ai-panel">
          <div class="br-ai-header">
            <i class="el-icon-magic-stick"></i>
            <span>AI 文档助手</span>
            <span class="br-ai-spacer"></span>
            <el-button size="mini" plain icon="el-icon-delete" @click="clearChat">清空</el-button>
          </div>
          <div class="br-ai-msgs" ref="aiMsgs">
            <div
              v-for="(m, idx) in messages"
              :key="idx"
              class="br-ai-msg"
              :class="m.role"
            >
              <div class="br-ai-avatar">{{ m.role === 'user' ? '我' : 'AI' }}</div>
              <div class="br-ai-bubble">
                <div class="br-ai-text" style="white-space: pre-wrap;">{{ m.text }}<span v-if="m.streaming" class="br-cursor">▌</span></div>
                <div v-if="m.role === 'ai' && m.suggestion && !m.streaming" class="br-ai-apply">
                  <el-button size="mini" type="primary" plain icon="el-icon-magic-stick" @click="applySuggestion(m)">
                    把建议应用到文档
                  </el-button>
                </div>
              </div>
            </div>
            <div v-if="aiThinking && messages.length === 0" class="br-ai-msg ai">
              <div class="br-ai-avatar">AI</div>
              <div class="br-ai-bubble"><i class="el-icon-loading"></i> 正在生成建议...</div>
            </div>
          </div>
          <div class="br-ai-input">
            <el-input
              v-model="inputText"
              type="textarea"
              :rows="2"
              resize="none"
              placeholder="例如：将第三章节的审批流程精简为 5 步，并加上责任人列"
              @keydown.native.enter.exact.prevent="sendChat"
            />
            <el-button
              type="primary"
              icon="el-icon-position"
              :loading="aiThinking"
              :disabled="!inputText.trim() || aiThinking"
              @click="sendChat"
            >
              发送
            </el-button>
          </div>
        </div>
      </section>

      <!-- ============ 右栏：工具侧边栏 ============ -->
      <aside class="br-right">
        <div class="br-tool-card">
          <div class="br-tool-title">文本格式</div>
          <div class="br-tool-row">
            <el-button size="mini" icon="el-icon-edit-outline" @click="exec('bold')" title="加粗"><b>B</b></el-button>
            <el-button size="mini" @click="exec('italic')" title="斜体"><i>I</i></el-button>
            <el-button size="mini" @click="exec('underline')" title="下划线"><u>U</u></el-button>
            <el-button size="mini" @click="exec('strikeThrough')" title="删除线"><s>S</s></el-button>
          </div>
          <div class="br-tool-row">
            <el-color-picker size="mini" v-model="textColor" @change="applyColor"></el-color-picker>
            <span class="br-tool-tip">字体颜色</span>
          </div>
          <div class="br-tool-row">
            <el-color-picker size="mini" v-model="hlColor" @change="applyHighlight"></el-color-picker>
            <span class="br-tool-tip">高亮背景</span>
          </div>
        </div>

        <div class="br-tool-card">
          <div class="br-tool-title">段落样式</div>
          <div class="br-tool-row wrap">
            <el-button size="mini" @click="formatBlock('H1')">H1</el-button>
            <el-button size="mini" @click="formatBlock('H2')">H2</el-button>
            <el-button size="mini" @click="formatBlock('H3')">H3</el-button>
            <el-button size="mini" @click="formatBlock('P')">正文</el-button>
          </div>
          <div class="br-tool-row wrap">
            <el-button size="mini" icon="el-icon-s-operation" @click="exec('insertUnorderedList')" title="无序"></el-button>
            <el-button size="mini" icon="el-icon-s-order" @click="exec('insertOrderedList')" title="有序"></el-button>
            <el-button size="mini" @click="exec('justifyLeft')" title="左对齐"><i class="el-icon-s-fold"></i></el-button>
            <el-button size="mini" @click="exec('justifyCenter')" title="居中"><i class="el-icon-s-grid"></i></el-button>
          </div>
        </div>

        <div class="br-tool-card">
          <div class="br-tool-title">操作</div>
          <div class="br-tool-row">
            <el-button size="mini" icon="el-icon-back" @click="exec('undo')">撤销</el-button>
            <el-button size="mini" icon="el-icon-right" @click="exec('redo')">重做</el-button>
          </div>
          <div class="br-tool-row">
            <el-button size="mini" icon="el-icon-search" @click="openFindReplace">查找替换</el-button>
            <el-button size="mini" icon="el-icon-refresh-left" @click="resetDoc">还原文档</el-button>
          </div>
        </div>

        <div class="br-tool-card outline">
          <div class="br-tool-title">
            文档大纲
            <el-button size="mini" plain icon="el-icon-refresh" @click="buildOutline">刷新</el-button>
          </div>
          <div class="br-outline">
            <div
              v-for="(o, i) in outline"
              :key="i"
              class="br-outline-item"
              :class="'lv-' + o.level"
              @click="jumpAnchor(o)"
            >
              <span class="br-outline-dot"></span>
              <span class="br-outline-text" :title="o.text">{{ o.text }}</span>
            </div>
            <div v-if="outline.length === 0" class="br-empty small">暂无大纲</div>
          </div>
        </div>
      </aside>
    </div>

    <!-- 查找替换弹层 -->
    <el-dialog
      :visible.sync="findReplaceVisible"
      title="查找与替换"
      width="380px"
      :append-to-body="true"
      custom-class="br-find-dialog"
    >
      <el-form size="small" label-width="60px">
        <el-form-item label="查找">
          <el-input v-model="findText" placeholder="要查找的内容"></el-input>
        </el-form-item>
        <el-form-item label="替换为">
          <el-input v-model="replaceText" placeholder="替换为"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="findReplaceVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="doReplaceAll">全部替换</el-button>
      </div>
    </el-dialog>

    <!-- 草稿箱抽屉 -->
    <el-drawer
      :visible.sync="draftDrawerVisible"
      title="我的草稿箱"
      direction="rtl"
      size="420px"
      :append-to-body="true"
      :modal-append-to-body="true"
      custom-class="br-draft-drawer"
    >
      <div class="br-draft-toolbar">
        <el-radio-group v-model="draftFilterScope" size="mini" @change="loadDraftList">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="current">当前模板</el-radio-button>
        </el-radio-group>
        <el-button size="mini" plain icon="el-icon-refresh" @click="loadDraftList">刷新</el-button>
      </div>
      <div v-loading="draftLoading" class="br-draft-list">
        <div v-for="d in drafts" :key="d.id" class="br-draft-item">
          <div class="br-draft-meta">
            <div class="br-draft-title" :title="d.title">{{ d.title || '未命名草稿' }}</div>
            <div class="br-draft-sub">
              <span class="br-tag">{{ d.templateTitle || '未关联模板' }}</span>
              <span v-if="d.baseVersionNo" class="br-tag">基于 v{{ d.baseVersionNo }}</span>
            </div>
            <div class="br-draft-time">{{ formatTime(d.updateTime || d.createTime) }}</div>
          </div>
          <div class="br-draft-ops">
            <el-button size="mini" type="primary" plain @click="viewDraft(d)">查看</el-button>
            <el-button size="mini" type="danger" plain @click="removeDraft(d)">删除</el-button>
          </div>
        </div>
        <div v-if="!draftLoading && drafts.length === 0" class="br-empty">暂无草稿</div>
      </div>
    </el-drawer>

    <!-- 历史版本抽屉 -->
    <el-drawer
      :visible.sync="versionDrawerVisible"
      title="历史版本"
      direction="rtl"
      size="460px"
      :append-to-body="true"
      :modal-append-to-body="true"
      custom-class="br-version-drawer"
    >
      <div class="br-draft-toolbar">
        <div class="br-ver-tpl">{{ currentDoc ? currentDoc.title : '--' }}</div>
        <el-button size="mini" plain icon="el-icon-refresh" @click="loadVersionList">刷新</el-button>
      </div>
      <div v-loading="versionLoading" class="br-draft-list">
        <div
          v-for="v in versions"
          :key="v.id"
          class="br-version-item"
          :class="{ 'is-latest': v.isLatest === 1, 'is-current': v.id === currentVersionId }"
        >
          <div class="br-version-head">
            <span class="br-version-no">v{{ v.versionNo }}</span>
            <el-tag v-if="v.isLatest === 1" size="mini" type="success" effect="plain">最新</el-tag>
            <el-tag v-if="v.id === currentVersionId" size="mini" type="warning" effect="plain">查看中</el-tag>
            <span class="br-version-time">{{ formatTime(v.createTime) }}</span>
          </div>
          <div class="br-version-meta">
            <span class="br-tag">{{ v.creatorName || v.creatorId || '系统' }}</span>
            <span v-if="v.changeSummary" class="br-version-summary" :title="v.changeSummary">{{ v.changeSummary }}</span>
            <span v-else class="br-version-summary muted">（无变更说明）</span>
          </div>
          <div class="br-version-ops">
            <el-button size="mini" type="primary" plain @click="viewVersion(v)">查看此版本</el-button>
            <el-button
              v-if="v.isLatest !== 1"
              size="mini"
              type="warning"
              plain
              @click="restoreVersion(v)"
            >恢复为新版</el-button>
          </div>
        </div>
        <div v-if="!versionLoading && versions.length === 0" class="br-empty">暂无历史版本</div>
      </div>
    </el-drawer>

    <!-- 新建需求对话框 -->
    <el-dialog
      :visible.sync="newTplDialogVisible"
      title="新建需求"
      width="560px"
      :append-to-body="true"
      custom-class="br-new-tpl-dialog"
    >
      <el-form ref="newTplForm" :model="newTpl" :rules="newTplRules" size="small" label-width="92px">
        <el-form-item label="需求编号" prop="templateNo">
          <el-input v-model="newTpl.templateNo" placeholder="可留空，留空将自动生成"></el-input>
        </el-form-item>
        <el-form-item label="需求标题" prop="title">
          <el-input v-model="newTpl.title" placeholder="如：研发费用归集需求"></el-input>
        </el-form-item>
        <el-form-item label="副标题">
          <el-input v-model="newTpl.subtitle" placeholder="如：内控方案 · 研发管理"></el-input>
        </el-form-item>
        <el-form-item label="需求描述">
          <el-input
            v-model="newTpl.description"
            type="textarea"
            :rows="4"
            placeholder="请描述本条需求的背景、目标与关键要点"
          ></el-input>
        </el-form-item>
        <el-form-item label="流程来源" prop="flowSource">
          <el-radio-group v-model="newTpl.flowSource" @change="onFlowSourceChange">
            <el-radio label="builtin">选择内置流程模板</el-radio>
            <el-radio label="custom">自定义（空白文档）</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="newTpl.flowSource === 'builtin'" label="已选流程">
          <div class="br-flow-pick-row">
            <span v-if="newTpl.selectedFlowTitle" class="br-flow-pick-name" :title="newTpl.selectedFlowTitle">
              {{ newTpl.selectedFlowTitle }}
            </span>
            <span v-else class="br-flow-pick-empty">尚未选择流程</span>
            <el-button size="mini" type="primary" plain icon="el-icon-folder-opened" @click="openFlowPicker">
              {{ newTpl.selectedFlowId ? '重新选择' : '选择流程' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="newTplDialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="newTplSubmitting" @click="submitNewTpl">保存需求</el-button>
      </div>
    </el-dialog>

    <!-- 流程选择子对话框（列表为现有左侧数据列表） -->
    <el-dialog
      :visible.sync="flowPickerVisible"
      title="选择内置流程模板"
      width="520px"
      :append-to-body="true"
      custom-class="br-flow-picker-dialog"
    >
      <div class="br-flow-picker-search">
        <el-input
          v-model="flowPickerSearch"
          size="small"
          placeholder="搜索流程模板"
          prefix-icon="el-icon-search"
          clearable
        />
      </div>
      <div class="br-flow-picker-list">
        <div
          v-for="item in flowPickerList"
          :key="item.id"
          class="br-flow-picker-item"
          :class="{ active: newTpl.selectedFlowId === item.id }"
          @click="pickFlow(item)"
        >
          <div class="br-flow-picker-no">{{ item.no }}</div>
          <div class="br-flow-picker-meta">
            <div class="br-flow-picker-title" :title="item.title">{{ item.title }}</div>
            <div class="br-flow-picker-sub">{{ item.subtitle || '内控流程' }}</div>
          </div>
          <i v-if="newTpl.selectedFlowId === item.id" class="el-icon-check br-flow-picker-check"></i>
        </div>
        <div v-if="flowPickerList.length === 0" class="br-empty">无匹配流程</div>
      </div>
      <div slot="footer">
        <el-button size="small" @click="flowPickerVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="confirmFlowPick">确定</el-button>
      </div>
    </el-dialog>

    <!-- 发布新版本对话框 -->
    <el-dialog
      :visible.sync="publishDialogVisible"
      title="发布新版本"
      width="460px"
      :append-to-body="true"
      custom-class="br-publish-dialog"
    >
      <el-form size="small" label-width="80px">
        <el-form-item label="模板">
          <span>{{ currentDoc ? currentDoc.title : '--' }}</span>
        </el-form-item>
        <el-form-item label="变更说明">
          <el-input
            v-model="publishSummary"
            type="textarea"
            :rows="3"
            placeholder="本次发布的变更说明，便于他人查阅"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="publishDialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="publishing" @click="confirmPublish">确认发布</el-button>
      </div>
    </el-dialog>

    <!-- 签字确认对话框 -->
    <el-dialog
      :visible.sync="confirmDialogVisible"
      title="签字确认"
      width="620px"
      :append-to-body="true"
      custom-class="br-confirm-dialog"
    >
      <div class="br-confirm-body">
        <el-form size="small" label-width="84px">
          <el-form-item label="需求文档">
            <span>{{ currentDoc ? currentDoc.title : '--' }}</span>
            <span v-if="currentVersionNo" class="br-confirm-ver">当前版本 v{{ currentVersionNo }}</span>
          </el-form-item>
          <el-form-item label="确认人">
            <el-input
              v-model="confirmForm.confirmerNames"
              placeholder="填写确认人姓名，多个用逗号分隔，如：张三,李四"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认意见">
            <el-input
              v-model="confirmForm.confirmOpinion"
              type="textarea"
              :rows="2"
              placeholder="可选，如：内容确认无误，同意进入开发"
            ></el-input>
          </el-form-item>
          <el-form-item label="手写签名">
            <div class="br-sign-wrap">
              <canvas
                ref="signCanvas"
                class="br-sign-canvas"
                width="500"
                height="160"
                @mousedown="signStart"
                @mousemove="signMove"
                @mouseup="signEnd"
                @mouseleave="signEnd"
                @touchstart="signStart"
                @touchmove="signMove"
                @touchend="signEnd"
              ></canvas>
              <div class="br-sign-actions">
                <span class="br-sign-tip">请在框内手写签名</span>
                <el-button size="mini" type="text" icon="el-icon-refresh-left" @click="clearSignature">清空</el-button>
              </div>
            </div>
          </el-form-item>
        </el-form>

        <!-- 历史确认记录 -->
        <div class="br-confirm-records" v-loading="confirmRecordsLoading">
          <div class="br-confirm-records-title">
            <i class="el-icon-time"></i> 历史确认记录
          </div>
          <div v-if="confirmRecords.length === 0 && !confirmRecordsLoading" class="br-confirm-empty">
            暂无确认记录
          </div>
          <div
            v-for="rec in confirmRecords"
            :key="rec.id"
            class="br-confirm-rec-item"
          >
            <div class="br-confirm-rec-main">
              <span class="br-confirm-rec-names">{{ rec.confirmerNames }}</span>
              <span class="br-confirm-rec-ver">v{{ rec.versionNo }}</span>
            </div>
            <div class="br-confirm-rec-sub">
              <span>{{ rec.operatorName || rec.operatorId }}</span>
              <span>{{ formatTime(rec.confirmTime) }}</span>
            </div>
            <div v-if="rec.confirmOpinion" class="br-confirm-rec-opinion">{{ rec.confirmOpinion }}</div>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" @click="confirmDialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="confirmSubmitting" @click="submitConfirm">
          提交确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 发送至业务蓝图进度对话框 -->
    <el-dialog
      :visible.sync="sendProgressVisible"
      title="正在发送至业务蓝图"
      width="440px"
      :append-to-body="true"
      :close-on-click-modal="false"
      :show-close="false"
      custom-class="br-send-progress-dialog"
    >
      <div class="br-send-progress">
        <el-progress
          type="circle"
          :percentage="sendProgressPercent"
          :width="120"
          :status="sendProgressPercent >= 100 ? 'success' : undefined"
        ></el-progress>
        <div class="br-send-progress-steps">
          <div
            v-for="(step, idx) in sendProgressSteps"
            :key="idx"
            class="br-send-progress-step"
            :class="{
              done: idx < sendProgressStep || sendProgressPercent >= 100,
              active: idx === sendProgressStep && sendProgressPercent < 100
            }"
          >
            <i
              :class="(idx < sendProgressStep || sendProgressPercent >= 100)
                ? 'el-icon-circle-check'
                : (idx === sendProgressStep ? 'el-icon-loading' : 'el-icon-more')"
            ></i>
            <span>{{ step }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import axios from 'axios'
import { chatStream } from '@/api/ai/starlight'
import AiInlineEdit from '@/views/index/components/AI/AiInlineEdit.vue'
import {
  getTemplateList,
  createTemplate,
  deleteTemplate,
  getLatestDoc,
  publishDocVersion,
  seedDocVersion,
  listDocVersions,
  getDocVersion,
  saveUserDraft,
  listUserDrafts,
  getUserDraft,
  deleteUserDraft,
  analyzeBlueprintRequirements,
  confirmDoc,
  getConfirmRecords
} from '@/api/ai/businessReview'

export default {
  name: 'BusinessReviewDialog',
  components: { AiInlineEdit },
  props: {
    visible: { type: Boolean, default: false }
  },
  data() {
    return {
      docList: [],          // 需求文档（nodeType=1）
      flowTemplates: [],    // 流程模版（nodeType≠1，供流程选择器用）
      searchKey: '',
      currentId: '',
      docHtml: '',
      originalHtml: '',
      currentVersionId: '',   // 当前文档对应的版本ID（用于草稿 baseVersion）
      currentVersionNo: null, // 当前文档对应的版本号
      readonly: false,
      fontSize: 15,
      // AI 占位
      messages: [],
      inputText: '',
      aiThinking: false,
      abortController: null,
      sessionId: 'bizreview_' + Date.now(),
      // 工具
      textColor: '#1f2937',
      hlColor: '#fff3a3',
      // 大纲
      outline: [],
      // 查找替换
      findReplaceVisible: false,
      findText: '',
      replaceText: '',
      // 草稿箱
      drafts: [],
      draftLoading: false,
      draftDrawerVisible: false,
      draftFilterScope: 'all', // all | current
      draftIndex: {},          // { templateId: count }
      // 历史版本相关
      versionDrawerVisible: false,
      versionLoading: false,
      versions: [],
      // 发送至业务蓝图
      sendingToBlueprint: false,
      // 发送进度（方案A 伪进度）
      sendProgressVisible: false,
      sendProgressPercent: 0,
      sendProgressStep: 0,
      sendProgressSteps: ['校验文档', '提取内容', 'AI 拆分需求', '组装结果', '完成'],
      _sendProgressTimer: null,
      // 划词快速提问（由 AiInlineEdit 组件接管）
      // 新建需求
      newTplDialogVisible: false,
      newTplSubmitting: false,
      newTpl: {
        templateNo: '',
        title: '',
        subtitle: '',
        description: '',
        flowSource: 'builtin',     // builtin | custom
        selectedFlowId: '',        // 选中的内置流程模板 id
        selectedFlowTitle: ''      // 选中的内置流程模板标题
      },
      newTplRules: {
        title: [{ required: true, message: '请输入需求标题', trigger: 'blur' }],
        flowSource: [{ required: true, message: '请选择流程来源', trigger: 'change' }]
      },
      // 流程选择子对话框
      flowPickerVisible: false,
      flowPickerSearch: '',
      // 发布版本
      publishDialogVisible: false,
      publishing: false,
      publishSummary: '',
      // 签字确认
      confirmDialogVisible: false,
      confirmSubmitting: false,
      confirmForm: {
        confirmerNames: '',
        confirmOpinion: ''
      },
      confirmRecords: [],       // 确认记录列表
      confirmRecordsLoading: false,
      _signCtx: null,           // canvas 2d 上下文
      _signDrawing: false,      // 是否正在书写
      _signHasInk: false,       // 画布是否有内容
      // 全局监听句柄
      _globalKeyHandler: null,
      _saveTimer: null
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(v) { this.$emit('update:visible', v) }
    },
    currentDoc() {
      return this.docList.find(d => d.id === this.currentId)
    },
    filteredList() {
      const k = (this.searchKey || '').trim().toLowerCase()
      if (!k) return this.docList
      return this.docList.filter(d => ((d.title || '') + (d.subtitle || '')).toLowerCase().includes(k))
    },
    flowPickerList() {
      const k = (this.flowPickerSearch || '').trim().toLowerCase()
      if (!k) return this.flowTemplates
      return this.flowTemplates.filter(d => ((d.title || '') + (d.subtitle || '') + (d.no || '')).toLowerCase().includes(k))
    },
    totalDraftCount() {
      let total = 0
      Object.keys(this.draftIndex).forEach(k => { total += (this.draftIndex[k] || 0) })
      return total
    },
    userInfo() {
      try {
        const raw = localStorage.getItem('userInfo')
        return raw ? JSON.parse(raw) : {}
      } catch (e) {
        return {}
      }
    },
    userId() {
      return this.userInfo.staffid || this.userInfo.staffId || this.userInfo.id || 'anonymous'
    },
    userName() {
      return this.userInfo.staffname || this.userInfo.staffName || this.userInfo.name || ''
    }
  },
  watch: {
    visible(val) {
      if (val) this.init()
    }
  },
  methods: {
    /* ===================== 初始化 ===================== */
    async init() {
      // 每次打开都重新拉取一次模板列表与草稿索引（保证最新）
      await this.loadIndex()
      await this.loadDraftIndex()
      if (this.docList.length > 0) {
        const stillExist = this.currentId && this.docList.some(d => d.id === this.currentId)
        if (stillExist) {
          await this.loadDocHtml(this.currentDoc)
        } else {
          await this.selectDoc(this.docList[0])
        }
      } else {
        this.currentId = ''
        this.docHtml = '<p style="color:#94a3b8">暂无可用模板，请点击右上角“新建模板”创建。</p>'
        this.originalHtml = this.docHtml
      }
      this.bindGlobalKey()
    },
    onOpened() {
      this.$nextTick(() => this.buildOutline())
    },
    onClosed() {
      this.unbindGlobalKey()
      if (this._sendProgressTimer) {
        clearInterval(this._sendProgressTimer)
        this._sendProgressTimer = null
      }
    },
    close() {
      this.dialogVisible = false
    },

    /* ===================== 数据加载 ===================== */
    async loadIndex() {
      try {
        const res = await getTemplateList()
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && Array.isArray(res.data)) {
          const all = res.data.map(t => ({
            id: t.id,
            no: t.templateNo || '',
            title: t.title || '',
            subtitle: t.subtitle || '',
            file: t.sourceFile || '',
            isSystem: t.isSystem || 0,
            sortNo: t.sortNo || 0,
            nodeType: t.nodeType,
            confirmStatus: t.confirmStatus || 0,
            confirmedVersionId: t.confirmedVersionId || ''
          }))
          this.docList = all.filter(t => t.nodeType === 1)          // 需求文档
          this.flowTemplates = all.filter(t => t.nodeType !== 1)    // 流程模版
          return
        }
        // 返回非预期格式时，记录并清空
        this.docList = []
        this.$message.warning('模板列表为空或格式异常')
      } catch (e) {
        this.$message.error('加载模板列表失败：' + (e.message || e))
        this.docList = []
      }
    },
    async selectDoc(item) {
      this.currentId = item.id
      await this.loadDocHtml(item)
      this.$nextTick(() => {
        this.buildOutline()
        if (this.$refs.docArea) this.$refs.docArea.scrollTop = 0
      })
    },
    /**
     * 文档加载策略：
     *   1) 先从后端读取最新版本（getLatestDoc），有则使用
     *   2) 后端无版本但模板带 sourceFile，则去静态文件读取并尝试种子写入到后端
     *   3) 其他情况显示空模板提示
     */
    async loadDocHtml(item) {
      this.currentVersionId = ''
      this.currentVersionNo = null
      // 1) 后端最新版本
      try {
        const res = await getLatestDoc(item.id)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && res.data && res.data.content) {
          this.docHtml = res.data.content
          this.originalHtml = res.data.content
          this.currentVersionId = res.data.id
          this.currentVersionNo = res.data.versionNo
          return
        }
      } catch (e) {
        // 静默：后端不可用时仍允许走静态 fallback
      }

      // 2) 静态 fallback + 种子写入
      let freshHtml = ''
      if (item.file) {
        try {
          const url = '/business-docs/' + item.file + '?t=' + Date.now()
          const res = await axios.get(url, { responseType: 'text' })
          freshHtml = this.sanitize(res.data)
        } catch (e) {
          freshHtml = ''
        }
      }
      if (freshHtml) {
        this.docHtml = freshHtml
        this.originalHtml = freshHtml
        // 种子写入：仅在该模板尚无版本时生效（后端做幂等判断）
        try {
          const seedRes = await seedDocVersion({
            templateId: item.id,
            content: freshHtml,
            creatorId: this.userId,
            creatorName: this.userName || '系统初始化'
          })
          if (seedRes && seedRes.data) {
            this.currentVersionId = seedRes.data.id
            this.currentVersionNo = seedRes.data.versionNo
          }
        } catch (e) {
          // 静默
        }
      } else {
        this.docHtml = `<h1>${item.title || '未命名'}</h1><p style="color:#94a3b8">该模板暂无内容，可直接编辑后点击“发布新版本”。</p>`
        this.originalHtml = this.docHtml
      }
    },
    sanitize(html) {
      if (!html) return ''
      let body = html
      const m = html.match(/<body[^>]*>([\s\S]*?)<\/body>/i)
      if (m) body = m[1]
      body = body.replace(/<script[\s\S]*?<\/script>/gi, '')
      body = body.replace(/<link[\s\S]*?>/gi, '')
      body = body.replace(/src="(?!https?:|data:|\/)/gi, 'src="/business-docs/')
      return body
    },

    /* ===================== 草稿（后端持久化） ===================== */
    hasDraft(id) {
      return (this.draftIndex[id] || 0) > 0
    },
    async loadDraftIndex() {
      try {
        const res = await listUserDrafts(this.userId)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && Array.isArray(res.data)) {
          const idx = {}
          res.data.forEach(d => {
            idx[d.templateId] = (idx[d.templateId] || 0) + 1
          })
          this.draftIndex = idx
        }
      } catch (e) {
        // 静默
      }
    },
    async loadDraftList() {
      this.draftLoading = true
      try {
        const tplFilter = this.draftFilterScope === 'current' ? this.currentId : undefined
        const res = await listUserDrafts(this.userId, tplFilter)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && Array.isArray(res.data)) {
          this.drafts = res.data
        } else {
          this.drafts = []
        }
      } catch (e) {
        this.$message.error('加载草稿列表失败：' + (e.message || e))
        this.drafts = []
      } finally {
        this.draftLoading = false
      }
    },
    /** 顶部「更多」下拉菜单命令分发 */
    handleMoreCommand(command) {
      if (command === 'saveDraft') this.saveDraft()
      else if (command === 'openDraftDrawer') this.openDraftDrawer()
      else if (command === 'openVersionDrawer') this.openVersionDrawer()
      else if (command === 'exportWord') this.exportWord()
    },

    /** 发送至业务蓝图：调后端接口分析全文，返回结构化需求条目后 emit 给父组件 */
    async sendToBlueprint() {
      if (!this.currentDoc) {
        this.$message.warning('请先选择一个模板')
        return
      }
      // 发送门禁：仅已签字确认的需求文档才允许发送至业务蓝图
      if (!this.currentDoc.confirmStatus || this.currentDoc.confirmStatus !== 1) {
        this.$message.warning('该需求文档尚未签字确认，确认后才能发送至业务蓝图')
        return
      }
      const docArea = this.$refs.docArea
      const docText = docArea ? (docArea.innerText || docArea.textContent || '') : this.docHtml
      if (!docText.trim()) {
        this.$message.warning('当前文档内容为空')
        return
      }

      this.sendingToBlueprint = true
      this.startSendProgress()
      try {
        const res = await analyzeBlueprintRequirements({
          title: this.currentDoc.title || '业务文档',
          content: docText
        })

        if (!res || !res.success) {
          this.stopSendProgress(false)
          this.$message.error('分析失败：' + (res && res.message ? res.message : '未知错误'))
          return
        }

        const requirements = res.requirements || []
        if (requirements.length === 0) {
          this.stopSendProgress(false)
          this.$message.warning('未能从文档中提取到有效需求，请检查文档内容是否规范')
          return
        }

        await this.finishSendProgress()
        this.$message.success(`已解析出 ${requirements.length} 条需求，正在打开业务蓝图`)
        this.$emit('send-to-blueprint', requirements)
      } catch (e) {
        console.error('[sendToBlueprint] 请求失败:', e)
        this.stopSendProgress(false)
        this.$message.error('请求失败，请稍后重试')
      } finally {
        this.sendingToBlueprint = false
      }
    },

    /* ===== 发送进度（方案A 伪进度） ===== */
    startSendProgress() {
      this.sendProgressVisible = true
      this.sendProgressPercent = 0
      this.sendProgressStep = 0
      if (this._sendProgressTimer) clearInterval(this._sendProgressTimer)
      // 伪进度：缓慢推进到 90%，留 10% 给真实完成
      this._sendProgressTimer = setInterval(() => {
        if (this.sendProgressPercent >= 90) return
        // 越接近 90 推进越慢
        const inc = this.sendProgressPercent < 60 ? 6 : 2
        this.sendProgressPercent = Math.min(90, this.sendProgressPercent + inc)
        const stepCount = this.sendProgressSteps.length
        this.sendProgressStep = Math.min(
          stepCount - 2,
          Math.floor(this.sendProgressPercent / (90 / (stepCount - 1)))
        )
      }, 220)
    },
    async finishSendProgress() {
      if (this._sendProgressTimer) {
        clearInterval(this._sendProgressTimer)
        this._sendProgressTimer = null
      }
      this.sendProgressPercent = 100
      this.sendProgressStep = this.sendProgressSteps.length - 1
      // 让用户看到 100% 完成态
      await new Promise(r => setTimeout(r, 350))
      this.sendProgressVisible = false
    },
    stopSendProgress(success) {
      if (this._sendProgressTimer) {
        clearInterval(this._sendProgressTimer)
        this._sendProgressTimer = null
      }
      this.sendProgressVisible = false
      this.sendProgressPercent = 0
      this.sendProgressStep = 0
    },

    /* ===================== 签字确认 ===================== */
    /** 打开签字确认对话框 */
    async openConfirmDialog() {
      if (!this.currentDoc) {
        this.$message.warning('请先选择一个需求文档')
        return
      }
      if (!this.currentVersionId) {
        this.$message.warning('当前文档暂无版本，无法确认')
        return
      }
      this.confirmForm = { confirmerNames: '', confirmOpinion: '' }
      this._signHasInk = false
      this.confirmDialogVisible = true
      // 加载历史确认记录
      this.loadConfirmRecords()
      // 等 DOM 渲染后初始化画布
      this.$nextTick(() => this.initSignaturePad())
    },
    /** 初始化签名画布 */
    initSignaturePad() {
      const canvas = this.$refs.signCanvas
      if (!canvas) return
      const ctx = canvas.getContext('2d')
      ctx.lineWidth = 2.2
      ctx.lineCap = 'round'
      ctx.lineJoin = 'round'
      ctx.strokeStyle = '#1f2937'
      // 清空为白底
      ctx.fillStyle = '#ffffff'
      ctx.fillRect(0, 0, canvas.width, canvas.height)
      this._signCtx = ctx
      this._signHasInk = false
    },
    /** 取相对画布坐标（兼容鼠标/触摸） */
    _signPos(e) {
      const canvas = this.$refs.signCanvas
      const rect = canvas.getBoundingClientRect()
      const point = (e.touches && e.touches[0]) ? e.touches[0] : e
      return {
        x: (point.clientX - rect.left) * (canvas.width / rect.width),
        y: (point.clientY - rect.top) * (canvas.height / rect.height)
      }
    },
    signStart(e) {
      if (!this._signCtx) return
      e.preventDefault()
      this._signDrawing = true
      const { x, y } = this._signPos(e)
      this._signCtx.beginPath()
      this._signCtx.moveTo(x, y)
    },
    signMove(e) {
      if (!this._signDrawing || !this._signCtx) return
      e.preventDefault()
      const { x, y } = this._signPos(e)
      this._signCtx.lineTo(x, y)
      this._signCtx.stroke()
      this._signHasInk = true
    },
    signEnd() {
      this._signDrawing = false
    },
    /** 清空签名 */
    clearSignature() {
      this.initSignaturePad()
    },
    /** 加载确认记录 */
    async loadConfirmRecords() {
      if (!this.currentId) return
      this.confirmRecordsLoading = true
      try {
        const res = await getConfirmRecords(this.currentId)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1)) {
          this.confirmRecords = res.data || []
        } else {
          this.confirmRecords = []
        }
      } catch (e) {
        console.error('[loadConfirmRecords] 失败:', e)
        this.confirmRecords = []
      } finally {
        this.confirmRecordsLoading = false
      }
    },
    /** 提交签字确认 */
    async submitConfirm() {
      const names = (this.confirmForm.confirmerNames || '').trim()
      if (!names) {
        this.$message.warning('请填写确认人姓名（多个用逗号分隔）')
        return
      }
      if (!this._signHasInk) {
        this.$message.warning('请在签名区域手写签名')
        return
      }
      const canvas = this.$refs.signCanvas
      const signatureImg = canvas ? canvas.toDataURL('image/png') : ''
      this.confirmSubmitting = true
      try {
        const res = await confirmDoc({
          templateId: this.currentId,
          signatureImg,
          confirmerNames: names,
          confirmOpinion: this.confirmForm.confirmOpinion || '',
          operatorId: this.userId,
          operatorName: this.userName
        })
        if (res && (res.code === 200 || res.code === 0 || res.code === 1)) {
          this.$message.success('签字确认成功，该需求已可发送至业务蓝图')
          // 本地同步确认态，避免重新拉列表
          if (this.currentDoc) {
            this.$set(this.currentDoc, 'confirmStatus', 1)
            if (res.data && res.data.versionId) {
              this.$set(this.currentDoc, 'confirmedVersionId', res.data.versionId)
            }
          }
          this.confirmDialogVisible = false
        } else {
          this.$message.error('确认失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        console.error('[submitConfirm] 失败:', e)
        this.$message.error('确认失败，请稍后重试')
      } finally {
        this.confirmSubmitting = false
      }
    },

    /** 导出 Word 文档 */
    exportWord() {
      if (!this.currentDoc) {
        this.$message.warning('请先选择一个模板')
        return
      }
      const html = this.$refs.docArea ? this.$refs.docArea.innerHTML : this.docHtml
      if (!html || !html.trim()) {
        this.$message.warning('当前文档内容为空')
        return
      }
      try {
        const htmlDocx = require('html-docx-js/dist/html-docx')
        const { saveAs } = require('file-saver')

        // 构造完整 HTML，注入 Word 兼容样式
        const title = this.currentDoc.title || '文档'
        const fullHtml = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>${title}</title>
  <style>
    body { font-family: "宋体", SimSun, serif; font-size: 12pt; line-height: 1.8; color: #000; margin: 0; padding: 0; }
    h1 { font-size: 22pt; font-weight: bold; text-align: center; margin: 24pt 0 12pt; }
    h2 { font-size: 16pt; font-weight: bold; margin: 18pt 0 9pt; }
    h3 { font-size: 14pt; font-weight: bold; margin: 14pt 0 7pt; }
    h4 { font-size: 12pt; font-weight: bold; margin: 12pt 0 6pt; }
    p  { margin: 0 0 8pt; text-indent: 2em; }
    ul, ol { margin: 6pt 0 6pt 24pt; padding: 0; }
    li { margin-bottom: 4pt; }
    table { border-collapse: collapse; width: 100%; margin: 10pt 0; }
    th, td { border: 1pt solid #000; padding: 4pt 8pt; font-size: 11pt; }
    th { background-color: #f0f0f0; font-weight: bold; text-align: center; }
    strong, b { font-weight: bold; }
    em, i { font-style: italic; }
    br { line-height: 1.8; }
  </style>
</head>
<body>${html}</body>
</html>`

        const blob = htmlDocx.asBlob(fullHtml)
        saveAs(blob, `${title}.docx`)
        this.$message.success('导出成功')
      } catch (e) {
        console.error('[exportWord] 导出失败:', e)
        this.$message.error('导出失败：' + (e.message || '未知错误'))
      }
    },
    async saveDraft() {
      if (!this.currentId) {
        this.$message.warning('请先选择一个模板')
        return
      }
      const html = this.$refs.docArea ? this.$refs.docArea.innerHTML : this.docHtml
      try {
        const res = await saveUserDraft({
          templateId: this.currentId,
          userId: this.userId,
          userName: this.userName,
          title: (this.currentDoc ? this.currentDoc.title : '') + ' · 草稿',
          content: html,
          baseVersionId: this.currentVersionId || null,
          baseVersionNo: this.currentVersionNo || null
        })
        if (res && (res.code === 200 || res.code === 0 || res.code === 1)) {
          this.$message.success('草稿已保存到我的草稿箱')
          // 更新计数
          this.$set(this.draftIndex, this.currentId, (this.draftIndex[this.currentId] || 0) + 1)
        } else {
          this.$message.error('保存失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        this.$message.error('保存失败：' + (e.message || e))
      }
    },
    onDocInput() {
      // 自动保存改为本地内存只缓存当前 docHtml，避免频繁打后端
      clearTimeout(this._saveTimer)
      this._saveTimer = setTimeout(() => {
        if (!this.$refs.docArea) return
        this.docHtml = this.$refs.docArea.innerHTML
      }, 800)
    },
    resetDoc() {
      this.$confirm('将放弃当前未发布的修改，恢复为最新发布版本，确认继续？', '提示', { type: 'warning' })
        .then(() => {
          if (this.currentDoc) this.loadDocHtml(this.currentDoc).then(() => this.buildOutline())
          this.$message.success('已还原为最新版本')
        }).catch(() => {})
    },
    openDraftDrawer() {
      this.draftDrawerVisible = true
      this.loadDraftList()
    },
    async viewDraft(d) {
      try {
        const res = await getUserDraft(d.id)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && res.data) {
          // 切换到对应模板（如果不是当前）
          if (d.templateId && d.templateId !== this.currentId) {
            const target = this.docList.find(x => x.id === d.templateId)
            if (target) this.currentId = target.id
          }
          this.docHtml = res.data.content || ''
          this.originalHtml = this.docHtml
          this.readonly = true
          this.draftDrawerVisible = false
          this.$message.info('正在查看草稿（只读），如需编辑请切换到“编辑”模式')
          this.$nextTick(() => this.buildOutline())
        }
      } catch (e) {
        this.$message.error('查看草稿失败：' + (e.message || e))
      }
    },
    async removeDraft(d) {
      try {
        await this.$confirm('确认删除该草稿？删除后不可恢复', '提示', { type: 'warning' })
        const res = await deleteUserDraft(d.id)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1)) {
          this.$message.success('已删除')
          // 维护计数
          if (this.draftIndex[d.templateId]) {
            const left = (this.draftIndex[d.templateId] || 1) - 1
            if (left <= 0) this.$delete(this.draftIndex, d.templateId)
            else this.$set(this.draftIndex, d.templateId, left)
          }
          this.loadDraftList()
        } else {
          this.$message.error('删除失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        if (e === 'cancel') return
        this.$message.error('删除失败：' + (e.message || e))
      }
    },

    /* ===================== 历史版本 ===================== */
    openVersionDrawer() {
      if (!this.currentId) {
        this.$message.warning('请先选择一个模板')
        return
      }
      this.versionDrawerVisible = true
      this.loadVersionList()
    },
    async loadVersionList() {
      if (!this.currentId) return
      this.versionLoading = true
      try {
        const res = await listDocVersions(this.currentId)
        if (res && res.code === 200) {
          this.versions = Array.isArray(res.data) ? res.data : []
        } else {
          this.versions = []
        }
      } catch (e) {
        this.versions = []
        this.$message.error('加载版本列表失败：' + (e.message || e))
      } finally {
        this.versionLoading = false
      }
    },
    async viewVersion(v) {
      if (!v || !v.id) return
      try {
        const res = await getDocVersion(v.id)
        if (res && res.code === 200 && res.data) {
          this.docHtml = res.data.content || ''
          this.originalHtml = this.docHtml
          this.currentVersionId = res.data.id || v.id
          this.currentVersionNo = res.data.versionNo || v.versionNo
          this.readonly = true
          this.versionDrawerVisible = false
          if (v.isLatest === 1) {
            this.$message.success('已切换到最新版本 v' + v.versionNo)
          } else {
            this.$message.info('正在查看历史版本 v' + v.versionNo + '（只读），如需作为新版请点“恢复为新版”')
          }
          this.$nextTick(() => this.buildOutline())
        } else {
          this.$message.error('加载版本内容失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        this.$message.error('加载版本内容失败：' + (e.message || e))
      }
    },
    async restoreVersion(v) {
      if (!v || !v.id) return
      try {
        await this.$confirm(
          '确定将版本 v' + v.versionNo + ' 的内容恢复发布为新版本吗？所有用户默认看到的“最新版本”将会更新。',
          '恢复历史版本',
          { type: 'warning', confirmButtonText: '确定恢复', cancelButtonText: '取消' }
        )
      } catch (e) {
        return
      }
      try {
        const detail = await getDocVersion(v.id)
        if (!detail || detail.code !== 200 || !detail.data) {
          this.$message.error('读取历史版本失败：' + (detail && detail.msg ? detail.msg : '未知错误'))
          return
        }
        const res = await publishDocVersion({
          templateId: this.currentId,
          content: detail.data.content || '',
          changeSummary: '恢复自 v' + v.versionNo,
          creatorId: this.userId,
          creatorName: this.userName
        })
        if (res && res.code === 200 && res.data) {
          this.$message.success('已恢复为新版本 v' + res.data.versionNo)
          this.docHtml = detail.data.content || ''
          this.originalHtml = this.docHtml
          this.currentVersionId = res.data.id
          this.currentVersionNo = res.data.versionNo
          this.readonly = true
          await this.loadVersionList()
          this.$nextTick(() => this.buildOutline())
        } else {
          this.$message.error('恢复失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        this.$message.error('恢复失败：' + (e.message || e))
      }
    },

    /* ===================== 发布新版本 ===================== */
    openPublishDialog() {
      if (!this.currentId) {
        this.$message.warning('请先选择一个模板')
        return
      }
      this.publishSummary = ''
      this.publishDialogVisible = true
    },
    async confirmPublish() {
      const html = this.$refs.docArea ? this.$refs.docArea.innerHTML : this.docHtml
      this.publishing = true
      try {
        const res = await publishDocVersion({
          templateId: this.currentId,
          content: html,
          changeSummary: this.publishSummary || '',
          creatorId: this.userId,
          creatorName: this.userName
        })
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && res.data) {
          this.$message.success('已发布为 v' + res.data.versionNo)
          this.currentVersionId = res.data.id
          this.currentVersionNo = res.data.versionNo
          this.publishDialogVisible = false
          this.originalHtml = html
          if (this.versionDrawerVisible) {
            this.loadVersionList()
          }
        } else {
          this.$message.error('发布失败：' + (res && res.msg ? res.msg : '未知错误'))
        }
      } catch (e) {
        this.$message.error('发布失败：' + (e.message || e))
      } finally {
        this.publishing = false
      }
    },

    /* ===================== 删除模板 ===================== */
    async handleDeleteTemplate(item) {
      try {
        await this.$confirm(`确定要删除模板「${item.title}」吗？删除后不可恢复。`, '删除确认', {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        })
      } catch {
        return
      }
      let res
      try {
        res = await deleteTemplate(item.id)
      } catch (e) {
        console.error('[BusinessReview] 删除模板接口异常:', e)
        this.$message.error('删除失败，请稍后重试')
        return
      }
      if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
        this.$message.success('模板已删除')
        // 若删除的是当前选中项，清空正文区
        if (this.currentId === item.id) {
          this.currentId = null
          this.docHtml = ''
        }
        // 刷新列表（失败不影响删除成功的提示）
        this.loadIndex().catch(() => {})
      } else {
        this.$message.error('删除失败：' + (res && res.msg ? res.msg : '未知错误'))
      }
    },

    /* ===================== 新建需求 ===================== */
    openNewTplDialog() {
      this.newTpl = {
        templateNo: '',
        title: '',
        subtitle: '',
        description: '',
        flowSource: 'builtin',
        selectedFlowId: '',
        selectedFlowTitle: ''
      }
      this.newTplDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.newTplForm) this.$refs.newTplForm.clearValidate()
      })
    },
    /** 切换流程来源：切到自定义时清空已选流程 */
    onFlowSourceChange(val) {
      if (val === 'custom') {
        this.newTpl.selectedFlowId = ''
        this.newTpl.selectedFlowTitle = ''
      }
    },
    /** 打开流程选择子对话框 */
    openFlowPicker() {
      if (!this.flowTemplates || this.flowTemplates.length === 0) {
        this.$message.warning('暂无可选流程模板，请先创建或切换为自定义')
        return
      }
      this.flowPickerSearch = ''
      this.flowPickerVisible = true
    },
    /** 选中某条流程（列表项点击） */
    pickFlow(item) {
      this.newTpl.selectedFlowId = item.id
      this.newTpl.selectedFlowTitle = item.title || item.no || ''
    },
    /** 确认流程选择 */
    confirmFlowPick() {
      if (!this.newTpl.selectedFlowId) {
        this.$message.warning('请先选择一个流程模板')
        return
      }
      this.flowPickerVisible = false
    },
    /** HTML 转义，防止描述内容破坏结构 */
    escapeHtml(str) {
      if (!str) return ''
      return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
    },
    /**
     * 获取指定模板的内容（仅用于"基于内置流程复制"）：
     *   1) 优先取后端最新版本
     *   2) 回退到静态文件
     *   3) 都没有则返回空字符串
     * 注意：本方法不修改组件当前文档状态。
     */
    async fetchTemplateContent(item) {
      if (!item) return ''
      try {
        const res = await getLatestDoc(item.id)
        if (res && (res.code === 200 || res.code === 0 || res.code === 1) && res.data && res.data.content) {
          return res.data.content
        }
      } catch (e) {
        // 静默，走静态 fallback
      }
      if (item.file) {
        try {
          const url = '/business-docs/' + item.file + '?t=' + Date.now()
          const res = await axios.get(url, { responseType: 'text' })
          return this.sanitize(res.data)
        } catch (e) {
          return ''
        }
      }
      return ''
    },
    /** 构造需求文档初始内容：需求说明块 +（可选）所选流程正文 */
    buildReqInitialContent(flowHtml) {
      const title = this.escapeHtml(this.newTpl.title)
      const subtitle = this.escapeHtml(this.newTpl.subtitle)
      const desc = this.escapeHtml(this.newTpl.description).replace(/\n/g, '<br/>')
      let block = '<h1>' + (title || '未命名需求') + '</h1>'
      if (subtitle) {
        block += '<p style="color:#64748b;margin-top:-6px;">' + subtitle + '</p>'
      }
      block += '<h2>需求说明</h2>'
      block += '<p>' + (desc || '<span style="color:#94a3b8">（暂无需求描述，可在此补充）</span>') + '</p>'
      if (flowHtml) {
        block += '<hr/><h2>关联流程</h2>' + flowHtml
      }
      return block
    },
    async submitNewTpl() {
      if (!this.$refs.newTplForm) return
      this.$refs.newTplForm.validate(async (valid) => {
        if (!valid) return
        // 选择内置流程时必须已选中一条
        if (this.newTpl.flowSource === 'builtin' && !this.newTpl.selectedFlowId) {
          this.$message.warning('请先选择一个内置流程模板，或切换为自定义')
          return
        }
        this.newTplSubmitting = true
        try {
          // 1) 创建需求节点（复用模板表）
          const createRes = await createTemplate({
            templateNo: this.newTpl.templateNo || '',
            title: this.newTpl.title,
            subtitle: this.newTpl.subtitle || '',
            sourceFile: '',
            isSystem: 0,
            nodeType: 1,                              // 标记为需求节点
            flowSource: this.newTpl.flowSource,       // custom / builtin
            sourceFlowId: this.newTpl.flowSource === 'builtin' ? this.newTpl.selectedFlowId : '',
            description: this.newTpl.description || '',
            creatorId: this.userId,
            creatorName: this.userName
          })
          if (!createRes || !(createRes.code === 200 || createRes.code === 0 || createRes.code === 1)) {
            this.$message.error('保存失败：' + (createRes && createRes.msg ? createRes.msg : '未知错误'))
            return
          }
          const newId = createRes.data && createRes.data.id

          // 2) 组装初始内容：内置→复制所选流程内容；自定义→仅需求说明
          let flowHtml = ''
          if (this.newTpl.flowSource === 'builtin' && this.newTpl.selectedFlowId) {
            // 流程模版来自 flowTemplates(nodeType!=1)，而非需求文档 docList(nodeType==1)
            const flowItem = this.flowTemplates.find(x => x.id === this.newTpl.selectedFlowId)
            flowHtml = await this.fetchTemplateContent(flowItem)
          }
          const initialContent = this.buildReqInitialContent(flowHtml)

          // 3) 发布为 v1
          if (newId) {
            await publishDocVersion({
              templateId: newId,
              content: initialContent,
              changeSummary: '需求创建初始版本',
              creatorId: this.userId,
              creatorName: this.userName
            })
          }

          this.$message.success('需求已保存')
          this.newTplDialogVisible = false
          await this.loadIndex()
          if (newId) {
            const target = this.docList.find(x => x.id === newId)
            if (target) await this.selectDoc(target)
          }
        } catch (e) {
          this.$message.error('保存失败：' + (e.message || e))
        } finally {
          this.newTplSubmitting = false
        }
      })
    },

    /* ===================== 时间格式化 ===================== */
    formatTime(t) {
      if (!t) return ''
      try {
        const d = typeof t === 'string' ? new Date(t.replace(/-/g, '/')) : new Date(t)
        const pad = n => (n < 10 ? '0' + n : '' + n)
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) +
               ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes())
      } catch (e) {
        return String(t)
      }
    },

    /* ===================== 防复制（静默拦截，不提示） ===================== */
    onForbidCopy() {
      /* 静默拦截，不弹提示 */
    },
    onKeydown(e) {
      const key = (e.key || '').toLowerCase()
      const meta = e.ctrlKey || e.metaKey
      if (meta && ['c', 'x', 'a', 'p'].includes(key)) {
        e.preventDefault()
        e.stopPropagation()
        return false
      }
      if (meta && key === 's') {
        // 保留 Ctrl+S 为快捷保存草稿
        e.preventDefault()
        this.saveDraft()
        return false
      }
      if (key === 'printscreen') {
        e.preventDefault()
      }
    },
    bindGlobalKey() {
      this.unbindGlobalKey()
      this._globalKeyHandler = (e) => {
        if (!this.dialogVisible) return
        const key = (e.key || '').toLowerCase()
        const meta = e.ctrlKey || e.metaKey
        if (meta && (key === 'c' || key === 'x') && this.isInsideDoc(e.target)) {
          e.preventDefault()
        }
      }
      window.addEventListener('keydown', this._globalKeyHandler, true)
    },
    unbindGlobalKey() {
      if (this._globalKeyHandler) {
        window.removeEventListener('keydown', this._globalKeyHandler, true)
        this._globalKeyHandler = null
      }
    },
    isInsideDoc(node) {
      const area = this.$refs.docArea
      if (!area || !node) return false
      return area === node || area.contains(node)
    },

    /* ===================== 工具栏 ===================== */
    exec(cmd) {
      this.focusDoc()
      try { document.execCommand(cmd, false, null) } catch (e) {}
      this.onDocInput()
    },
    formatBlock(tag) {
      this.focusDoc()
      try { document.execCommand('formatBlock', false, tag) } catch (e) {}
      this.onDocInput()
      this.buildOutline()
    },
    applyColor(c) {
      this.focusDoc()
      try { document.execCommand('foreColor', false, c) } catch (e) {}
      this.onDocInput()
    },
    applyHighlight(c) {
      this.focusDoc()
      try {
        document.execCommand('hiliteColor', false, c) ||
        document.execCommand('backColor', false, c)
      } catch (e) {}
      this.onDocInput()
    },
    focusDoc() {
      if (this.readonly) {
        this.$message.info('当前为只读模式，请先切换到编辑')
        return
      }
      if (this.$refs.docArea) this.$refs.docArea.focus()
    },
    toggleReadonly() {
      this.readonly = !this.readonly
    },
    changeFontSize(delta) {
      const next = Math.min(28, Math.max(12, this.fontSize + delta))
      this.fontSize = next
    },
    openFindReplace() {
      this.findText = ''
      this.replaceText = ''
      this.findReplaceVisible = true
    },
    doReplaceAll() {
      if (!this.findText) return this.$message.warning('请输入要查找的内容')
      const area = this.$refs.docArea
      if (!area) return
      const safe = this.findText.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
      const re = new RegExp(safe, 'g')
      area.innerHTML = area.innerHTML.replace(re, this.replaceText || '')
      this.onDocInput()
      this.buildOutline()
      this.findReplaceVisible = false
      this.$message.success('替换完成')
    },

    /* ===================== 大纲 ===================== */
    buildOutline() {
      const area = this.$refs.docArea
      if (!area) return (this.outline = [])
      const nodes = area.querySelectorAll('h1,h2,h3,h4')
      const list = []
      nodes.forEach((n, i) => {
        if (!n.id) n.id = 'br-anchor-' + i
        list.push({
          id: n.id,
          level: parseInt(n.tagName.substring(1), 10),
          text: (n.innerText || n.textContent || '').trim().slice(0, 60)
        })
      })
      this.outline = list
    },
    jumpAnchor(o) {
      const el = document.getElementById(o.id)
      if (el && this.$refs.docArea) {
        this.$refs.docArea.scrollTo({ top: el.offsetTop - 12, behavior: 'smooth' })
      }
    },

    /* ===================== AI 对话（流式） ===================== */
    greetIfEmpty() {
      if (this.messages.length === 0) {
        this.messages.push({
          role: 'ai',
          text: '你好！我是 AI 文档助手。你可以让我精简章节、补全责任人、调整标题层级或重写段落。发送消息后，我会结合当前文档内容给出建议，并可一键应用到文档。'
        })
      }
    },
    sendChat() {
      const text = (this.inputText || '').trim()
      if (!text || this.aiThinking) return

      // 获取当前文档内容（纯文本，避免 HTML 标签干扰）
      const docArea = this.$refs.docArea
      const docText = docArea ? (docArea.innerText || docArea.textContent || '') : ''
      const docTitle = this.currentDoc ? this.currentDoc.title : '当前文档'

      // 构建发送给 AI 的完整消息：明确要求修改文档
      const fullMessage = `你是一个专业的内控流程文档助手。当前正在编辑的文档是《${docTitle}》。

【文档当前内容】
${docText}

【用户需求】
${text}

请根据用户需求，对文档内容进行修改或优化，并明确输出修改后的完整文档内容（使用 HTML 格式，以便直接应用到文档编辑器）。请在回复末尾用 <<<DOC_START>>> 和 <<<DOC_END>>> 包裹修改后的 HTML 文档内容。`

      this.inputText = ''
      this._runAiChat(text, fullMessage)
    },

    _runAiChat(userVisibleText, fullPrompt) {
      if (this.aiThinking) return
      // 添加用户消息
      this.messages.push({ role: 'user', text: userVisibleText })
      this.scrollChatBottom()

      // 添加 AI 回复占位（流式追加）
      const aiMsgIndex = this.messages.length
      this.messages.push({
        role: 'ai',
        text: '',
        streaming: true,
        suggestion: ''
      })
      this.aiThinking = true
      this.scrollChatBottom()

      // 如果有上一个请求，先中止
      if (this.abortController) {
        this.abortController.abort()
      }

      // 调用流式接口
      this.abortController = chatStream(
        {
          message: fullPrompt,
          sessionId: this.sessionId,
          enableThinking: false,
          enableWebSearch: false,
          stream: true
        },
        {
          onThinking: () => {},
          onMessage: (content) => {
            this.$set(this.messages, aiMsgIndex, {
              ...this.messages[aiMsgIndex],
              text: this.messages[aiMsgIndex].text + content,
              streaming: true
            })
            this.scrollChatBottom()
          },
          onProgress: () => {},
          onToolCall: () => {},
          onError: (error) => {
            this.$set(this.messages, aiMsgIndex, {
              ...this.messages[aiMsgIndex],
              text: this.messages[aiMsgIndex].text || `请求失败：${error}`,
              streaming: false
            })
            this.aiThinking = false
            this.abortController = null
          },
          onComplete: () => {
            // 流式完成，直接把 AI 完整回复作为可应用的建议内容
            const fullText = this.messages[aiMsgIndex].text
            const startTag = '<<<DOC_START>>>'
            const endTag = '<<<DOC_END>>>'
            const startIdx = fullText.indexOf(startTag)
            const endIdx = fullText.indexOf(endTag)

            let suggestion = ''
            let displayText = fullText

            if (startIdx !== -1 && endIdx !== -1 && endIdx > startIdx) {
              // AI 按格式返回了 HTML 块，提取出来
              suggestion = fullText.substring(startIdx + startTag.length, endIdx).trim()
              displayText = fullText.substring(0, startIdx).trim()
              if (!displayText) displayText = '已生成文档修改建议，点击下方按钮应用到文档。'
            } else {
              // AI 没有按格式返回，把完整回复内容作为 suggestion（直接覆盖文档）
              suggestion = fullText
              displayText = fullText
            }

            this.$set(this.messages, aiMsgIndex, {
              ...this.messages[aiMsgIndex],
              text: displayText,
              suggestion: suggestion,
              streaming: false
            })
            this.aiThinking = false
            this.abortController = null
            this.scrollChatBottom()
          }
        }
      )
    },

    /* ===================== 划词快速提问 ===================== */
    onDocMouseUp(e) {
      setTimeout(() => {
        const sel = window.getSelection ? window.getSelection() : null
        if (!sel || sel.isCollapsed) return
        const text = (sel.toString() || '').trim()
        if (!text) return

        let savedRange = null
        try {
          savedRange = sel.getRangeAt(0).cloneRange()
        } catch (err) {
          return
        }

        // 调用复用组件，传入选区、选中文本和替换回调
        this.$refs.aiInlineEdit && this.$refs.aiInlineEdit.show({
          savedRange,
          selectedText: text,
          onReplace: (content, range) => {
            try {
              if (this.readonly) this.readonly = false
              const s = window.getSelection()
              s.removeAllRanges()
              s.addRange(range)
              range.deleteContents()
              const frag = document.createDocumentFragment()
              content.split('\n').forEach((line, i, arr) => {
                frag.appendChild(document.createTextNode(line))
                if (i < arr.length - 1) frag.appendChild(document.createElement('br'))
              })
              range.insertNode(frag)
              s.collapseToEnd()
              this.onDocInput()
              this.$message.success('已替换选中内容')
            } catch (e) {
              console.error('[QuickAsk] 替换失败:', e)
              this.$message.error('替换失败，请手动粘贴')
            }
          }
        })
      }, 0)
    },
  }
}
</script>

<style scoped>
/* 弹窗整体 */
.business-review-dialog >>> .el-dialog {
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.18);
  display: flex;
  flex-direction: column;
  max-height: 92vh;
  margin-bottom: 0 !important;
}
.business-review-dialog >>> .el-dialog__header {
  padding: 0;
  border-bottom: 1px solid #eef2f7;
  flex-shrink: 0;
}
.business-review-dialog >>> .el-dialog__body {
  padding: 0;
  flex: 1;
  min-height: 0;
  background: #f7fafc;
  overflow: hidden;
}

/* 头部 */
.br-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 18px;
  background: linear-gradient(135deg, #ecfdf5 0%, #f0fdf4 60%, #fff 100%);
}
.br-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 16px;
  color: #064e3b;
}
.br-title i {
  font-size: 20px;
  color: #10b981;
}
.br-actions { display: flex; gap: 8px; }

/* 三栏 */
.br-body {
  display: flex;
  height: 100%;
  min-height: 0;
  gap: 12px;
  padding: 12px;
  box-sizing: border-box;
}

/* 左栏 */
.br-left {
  width: 240px;
  flex-shrink: 0;
  min-height: 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.br-left-search {
  padding: 10px;
  border-bottom: 1px solid #eef2f7;
}
.br-left-list {
  flex: 1;
  overflow-y: auto;
  padding: 6px;
}
.br-doc-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 10px;
  border-radius: 10px;
  cursor: pointer;
  transition: all .2s;
  margin-bottom: 4px;
  position: relative;
}
.br-doc-item:hover { background: #f0fdf4; }
.br-doc-item.active {
  background: linear-gradient(135deg, #d1fae5, #ecfdf5);
  box-shadow: 0 2px 6px rgba(16, 185, 129, 0.15);
}
.br-doc-no {
  width: 28px; height: 28px;
  border-radius: 8px;
  background: #10b981; color: #fff;
  display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 600;
  flex-shrink: 0;
}
.br-doc-item:not(.active) .br-doc-no {
  background: #e5e7eb; color: #475569;
}
.br-doc-meta { flex: 1; min-width: 0; }
.br-doc-title {
  font-size: 13px;
  color: #0f172a;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.br-doc-sub {
  font-size: 11px; color: #94a3b8; margin-top: 2px;
}
.br-doc-mark { color: #f59e0b; font-size: 14px; }
.br-doc-delete {
  font-size: 14px;
  color: #c0c4cc;
  opacity: 0;
  transition: opacity 0.15s, color 0.15s;
  flex-shrink: 0;
  padding: 2px;
}
.br-doc-item:hover .br-doc-delete {
  opacity: 1;
}
.br-doc-delete:hover {
  color: #f56c6c;
}
.br-empty {
  text-align: center;
  color: #94a3b8;
  font-size: 13px;
  padding: 30px 0;
}
.br-empty.small { padding: 16px 0; font-size: 12px; }

/* 中栏 */
.br-center {
  flex: 1;
  min-width: 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.br-doc-header {
  flex-shrink: 0;
  background: #fff;
  border-radius: 12px;
  padding: 10px 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.br-doc-h-left { display: flex; align-items: center; gap: 8px; min-width: 0; }
.br-doc-h-no {
  background: #10b981; color: #fff;
  padding: 2px 8px; border-radius: 6px; font-size: 12px;
}
.br-doc-h-title {
  font-size: 15px; font-weight: 600; color: #0f172a;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.br-doc-h-right { display: flex; align-items: center; gap: 6px; }
.br-fontsize { font-size: 12px; color: #64748b; min-width: 36px; text-align: center; }

.br-doc-content {
  flex: 4 1 0;
  min-height: 0;
  background: #fff;
  border-radius: 12px;
  padding: 22px 28px;
  overflow-y: auto;
  outline: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  line-height: 1.8;
  color: #1f2937;
  -webkit-user-drag: none;
}
.br-doc-content.readonly {
  background: #fafbfc;
}
.br-doc-content >>> h1 { font-size: 22px; margin: 18px 0 12px; color: #064e3b; }
.br-doc-content >>> h2 { font-size: 18px; margin: 16px 0 10px; color: #065f46; }
.br-doc-content >>> h3 { font-size: 16px; margin: 14px 0 8px; color: #047857; }
.br-doc-content >>> table {
  border-collapse: collapse; width: 100%; margin: 10px 0;
}
.br-doc-content >>> th, .br-doc-content >>> td {
  border: 1px solid #e2e8f0; padding: 6px 10px; font-size: 13px;
}
.br-doc-content >>> th { background: #f0fdf4; }
.br-doc-content >>> img { max-width: 100%; }

/* 防复制提示水印（弱化） */
.no-copy {
  position: relative;
}
.no-copy::before {
  content: '';
  position: absolute; inset: 0;
  pointer-events: none;
  background-image: repeating-linear-gradient(
    -30deg,
    rgba(16, 185, 129, 0.04) 0,
    rgba(16, 185, 129, 0.04) 80px,
    transparent 80px, transparent 160px
  );
}

/* AI 面板 */
.br-ai-panel {
  flex: 1 1 0;
  min-height: 0;
  background: #fff;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}
.br-ai-header {
  flex-shrink: 0;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid #eef2f7;
  background: #f8fafc;
  font-weight: 600;
  font-size: 13px;
  color: #334155;
}
.br-ai-header i { color: #10b981; }
.br-ai-spacer { flex: 1; }
.br-ai-msgs {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 8px 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.br-ai-msg {
  display: flex; gap: 8px; align-items: flex-start;
}
.br-ai-msg.user { flex-direction: row-reverse; }
.br-ai-avatar {
  width: 26px; height: 26px;
  border-radius: 50%;
  background: #e2e8f0;
  font-size: 11px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  color: #475569;
}
.br-ai-msg.ai .br-ai-avatar { background: #d1fae5; color: #065f46; }
.br-ai-msg.user .br-ai-avatar { background: #dbeafe; color: #1e40af; }
.br-ai-bubble {
  max-width: 76%;
  background: #f1f5f9;
  padding: 8px 12px;
  border-radius: 10px;
  font-size: 13px;
  white-space: pre-wrap;
  word-break: break-word;
}
.br-ai-msg.user .br-ai-bubble { background: #e0f2fe; }
.br-ai-msg.ai .br-ai-bubble { background: #f0fdf4; }
.br-ai-apply { margin-top: 6px; }
.br-ai-input {
  flex-shrink: 0;
  display: flex;
  gap: 8px;
  padding: 8px;
  border-top: 1px solid #eef2f7;
  background: #fff;
}
.br-ai-input >>> .el-textarea__inner {
  border-radius: 8px;
}

/* 流式光标 */
.br-cursor {
  display: inline-block;
  animation: br-blink 0.8s step-end infinite;
  color: #10b981;
  font-weight: bold;
}
@keyframes br-blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

/* 右栏 */
.br-right {
  width: 260px;
  flex-shrink: 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
  overflow-y: auto;
}
.br-tool-card {
  background: #fff;
  border-radius: 12px;
  padding: 10px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.br-tool-card.outline { flex: 1; min-height: 0; display: flex; flex-direction: column; }
.br-tool-title {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
  letter-spacing: .5px;
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.br-tool-row {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 6px;
}
.br-tool-row.wrap { flex-wrap: wrap; }
.br-tool-tip { font-size: 12px; color: #64748b; }
.br-outline {
  flex: 1;
  overflow-y: auto;
  padding-right: 4px;
}
.br-outline-item {
  font-size: 12px;
  color: #334155;
  padding: 4px 6px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background .2s;
}
.br-outline-item:hover { background: #f0fdf4; color: #065f46; }
.br-outline-item.lv-2 { padding-left: 14px; }
.br-outline-item.lv-3 { padding-left: 22px; color: #64748b; }
.br-outline-item.lv-4 { padding-left: 30px; color: #94a3b8; }
.br-outline-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: #10b981;
  flex-shrink: 0;
}
.br-outline-item.lv-2 .br-outline-dot { background: #34d399; }
.br-outline-item.lv-3 .br-outline-dot { background: #a7f3d0; }
.br-outline-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 滚动条 */
.br-doc-content::-webkit-scrollbar,
.br-ai-msgs::-webkit-scrollbar,
.br-left-list::-webkit-scrollbar,
.br-outline::-webkit-scrollbar,
.br-right::-webkit-scrollbar {
  width: 6px;
}
.br-doc-content::-webkit-scrollbar-thumb,
.br-ai-msgs::-webkit-scrollbar-thumb,
.br-left-list::-webkit-scrollbar-thumb,
.br-outline::-webkit-scrollbar-thumb,
.br-right::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

/* 新增：草稿箱抽屉与新模板/发布弹层样式 */
.br-actions .br-badge >>> .el-badge__content {
  transform: translateY(-6px) translateX(4px) scale(0.85);
}
/* 更多下拉菜单内角标 */
.br-more-badge {
  margin-left: 6px;
}
.br-more-badge >>> .el-badge__content {
  transform: translateY(-6px) translateX(4px) scale(0.85);
}
.br-draft-drawer >>> .el-drawer__header {
  margin-bottom: 8px;
  padding: 14px 18px 10px;
  border-bottom: 1px solid #f0f1f4;
  font-weight: 600;
}
.br-draft-toolbar {
  padding: 8px 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #f0f1f4;
}
.br-draft-list {
  padding: 8px 12px 16px;
  max-height: calc(100vh - 130px);
  overflow-y: auto;
}
.br-draft-item {
  border: 1px solid #eef0f3;
  border-radius: 8px;
  padding: 10px 12px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  transition: box-shadow .15s, border-color .15s;
}
.br-draft-item:hover {
  border-color: #c7d2fe;
  box-shadow: 0 4px 14px rgba(99, 102, 241, .08);
}
.br-draft-meta {
  flex: 1;
  min-width: 0;
}
.br-draft-title {
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.br-draft-sub {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 4px;
}
.br-tag {
  font-size: 12px;
  background: #f3f4f6;
  color: #6b7280;
  padding: 1px 8px;
  border-radius: 10px;
}
.br-draft-time {
  color: #9ca3af;
  font-size: 12px;
}
.br-draft-ops {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-left: 10px;
}
.br-empty.small {
  font-size: 12px;
}
.br-ver-tag {
  display: inline-block;
  margin-left: 6px;
  padding: 0 6px;
  height: 18px;
  line-height: 18px;
  font-size: 12px;
  color: #409eff;
  background: #ecf5ff;
  border-radius: 9px;
  vertical-align: middle;
}
.br-version-drawer >>> .el-drawer__header {
  margin-bottom: 8px;
  padding: 14px 18px 10px;
  border-bottom: 1px solid #f0f1f4;
}
.br-ver-tpl {
  font-size: 13px;
  color: #303133;
  font-weight: 600;
  max-width: 260px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.br-version-item {
  padding: 12px 14px;
  border-bottom: 1px solid #f0f1f4;
  transition: background 0.15s;
}
.br-version-item:hover {
  background: #fafbfc;
}
.br-version-item.is-current {
  background: #fff8e6;
}
.br-version-head {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}
.br-version-no {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
.br-version-time {
  margin-left: auto;
  color: #9ca3af;
  font-size: 12px;
}
.br-version-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
.br-version-summary {
  font-size: 12px;
  color: #606266;
  flex: 1;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.br-version-summary.muted {
  color: #c0c4cc;
  font-style: italic;
}
.br-version-ops {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

/* ============ 划词快速提问浮窗 ============ */
.br-quick-ask.wk-chat {
  position: fixed;
  width: 380px;
  z-index: 3000;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 8px 28px rgba(15, 42, 78, 0.18), 0 2px 6px rgba(15, 42, 78, 0.08);
  border: 1px solid rgba(64, 158, 255, 0.18);
  padding: 10px 12px 12px;
  animation: brQuickAskIn 0.16s ease-out;
}
@keyframes brQuickAskIn {
  from { opacity: 0; transform: translateY(-4px); }
  to   { opacity: 1; transform: translateY(0);    }
}
.br-quick-ask .middle-card {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}
.br-quick-ask .chat-entry-wrapper_logo {
  flex: 0 0 28px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  margin-top: 4px;
  background: linear-gradient(135deg, #409eff 0%, #5cd29f 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.br-quick-ask .chat-entry-wrapper_logo::after {
  content: '✦';
  color: #fff;
  font-size: 14px;
  line-height: 1;
}
.br-quick-ask .entry-event {
  flex: 1;
  min-width: 0;
}
.br-quick-ask .submit-wrap {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.br-quick-ask .qa-selected {
  font-size: 12px;
  color: #606266;
  background: #f3f7ff;
  border-left: 3px solid #409eff;
  padding: 4px 8px;
  border-radius: 4px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.br-quick-ask .el-input__inner {
  height: 32px;
  line-height: 32px;
  border-radius: 8px;
  padding-right: 32px;
}
.br-quick-ask .el-input__suffix-inner .el-input__icon {
  cursor: pointer;
  font-size: 16px;
  color: #409eff;
  transition: transform 0.15s;
}
.br-quick-ask .el-input__suffix-inner .el-input__icon:hover {
  transform: scale(1.15);
}

/* 新建需求：流程来源选择行 */
.br-flow-pick-row {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}
.br-flow-pick-name {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #1f2937;
  font-weight: 500;
  background: #f3f7ff;
  border-left: 3px solid #409eff;
  padding: 4px 10px;
  border-radius: 4px;
}
.br-flow-pick-empty {
  flex: 1;
  color: #94a3b8;
}

/* 流程选择子对话框 */
.br-flow-picker-search {
  margin-bottom: 10px;
}
.br-flow-picker-list {
  max-height: 380px;
  overflow-y: auto;
  padding-right: 2px;
}
.br-flow-picker-item {
  display: flex;
  align-items: center;
  gap: 10px;
  border: 1px solid #eef0f3;
  border-radius: 8px;
  padding: 10px 12px;
  margin-bottom: 8px;
  cursor: pointer;
  background: #fff;
  transition: box-shadow .15s, border-color .15s, background .15s;
}
.br-flow-picker-item:hover {
  border-color: #c7d2fe;
  box-shadow: 0 4px 14px rgba(99, 102, 241, .08);
}
.br-flow-picker-item.active {
  border-color: #409eff;
  background: #f3f7ff;
}
.br-flow-picker-no {
  font-size: 12px;
  color: #6b7280;
  min-width: 56px;
}
.br-flow-picker-meta {
  flex: 1;
  min-width: 0;
}
.br-flow-picker-title {
  font-weight: 600;
  color: #1f2937;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.br-flow-picker-sub {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.br-flow-picker-check {
  color: #409eff;
  font-size: 18px;
}

/* ===================== 签字确认 ===================== */
.br-doc-confirmed {
  color: #22c55e;
  font-size: 15px;
  margin-left: 4px;
  flex-shrink: 0;
}
.br-confirm-body {
  max-height: 62vh;
  overflow-y: auto;
}
.br-confirm-ver {
  margin-left: 10px;
  font-size: 12px;
  color: #f59e0b;
}
.br-sign-wrap {
  width: 100%;
}
.br-sign-canvas {
  width: 100%;
  height: 160px;
  border: 1px dashed #cbd5e1;
  border-radius: 6px;
  background: #fff;
  cursor: crosshair;
  touch-action: none;
}
.br-sign-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 4px;
}
.br-sign-tip {
  font-size: 12px;
  color: #9ca3af;
}
.br-confirm-records {
  margin-top: 10px;
  border-top: 1px solid #eef0f3;
  padding-top: 10px;
}
.br-confirm-records-title {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 8px;
}
.br-confirm-empty {
  font-size: 12px;
  color: #9ca3af;
  padding: 8px 0;
}
.br-confirm-rec-item {
  background: #f8fafc;
  border: 1px solid #eef0f3;
  border-radius: 6px;
  padding: 8px 10px;
  margin-bottom: 6px;
}
.br-confirm-rec-main {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.br-confirm-rec-names {
  font-size: 13px;
  font-weight: 600;
  color: #1f2937;
}
.br-confirm-rec-ver {
  font-size: 12px;
  color: #f59e0b;
}
.br-confirm-rec-sub {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  color: #94a3b8;
  margin-top: 2px;
}
.br-confirm-rec-opinion {
  font-size: 12px;
  color: #64748b;
  margin-top: 4px;
}

/* ===================== 发送进度 ===================== */
.br-send-progress {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 8px 4px;
}
.br-send-progress-steps {
  flex: 1;
}
.br-send-progress-step {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #94a3b8;
  padding: 4px 0;
  transition: color 0.2s;
}
.br-send-progress-step i {
  font-size: 15px;
}
.br-send-progress-step.active {
  color: #409eff;
  font-weight: 600;
}
.br-send-progress-step.done {
  color: #22c55e;
}
</style>
