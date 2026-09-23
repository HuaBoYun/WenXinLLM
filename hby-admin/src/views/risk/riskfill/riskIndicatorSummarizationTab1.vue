<template>
  <div class="risk-indicator-summarization">
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item v-for="(item, index) in searchItem" :key="index">
            <div v-if="item.name === '公司'" style="display: flex">
              <el-input
                placeholder="请输入公司名称"
                disabled
                v-model="queryForm.companyName"
                style="margin-right: 10px"
                width="100%"
              />
              <el-button type="primary" @click="$refs.companyTree.showEdit()">
                选择
              </el-button>
            </div>
            <el-select
              v-model="queryForm.jd"
              clearable
              placeholder="请选择季度"
              v-if="item.name === '季度'"
            >
              <el-option
                v-for="option in quarterOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              ></el-option>
            </el-select>
            <el-date-picker
              v-if="item.name === '年度'"
              v-model="queryForm.year"
              type="year"
              format="yyyy"
              value-format="yyyy"
              placeholder="年度"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetSearch()" type="primary">重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-tooltip
              class="item"
              effect="dark"
              content="搜索筛选"
              placement="top"
            >
              <el-popover placement="left" trigger="click">
                <filter-search
                  v-if="true"
                  :list="searchAll"
                  :name="localKey"
                  @updateSearchShow="initSearch"
                />
                <el-button slot="reference" style="height: 32px">
                  <vab-icon icon="filter" :is-custom-svg="true" />
                </el-button>
              </el-popover>
            </el-tooltip>
          </el-form-item>
          <el-form-item>
            <span
              :class="searchMore ? 'search-more is-opened' : 'search-more'"
              @click="showMore"
            >
              <span>{{ searchMore ? '收起' : '展开' }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <div class="risk-container">
      <el-button type="primary" @click="handleExport">导出</el-button>
      <!-- 表格标题 -->
      <div class="table-header">
        <h2>{{ dynamicTitle }}</h2>
        <div class="reporting-info">
          <span>填报单位：{{ reportingUnit }}</span>
          <span>填报时间：{{ reportingDate }}</span>
          <span>填报人：{{ reporter }}</span>
          <span>联系方式：{{ contact }}</span>
        </div>
      </div>

      <!-- 主要风险表格 -->
      <div class="risk-table-container">
        <table class="risk-table">
          <!-- 多级表头 -->
          <thead>
            <tr>
              <th rowspan="2">一级风险</th>
              <th rowspan="2">二级风险</th>
              <th rowspan="2" colspan="2">风险监测预警指标</th>
              <th colspan="3">季度同比增减率</th>
              <th colspan="5">季度环比增减率</th>
              <th rowspan="2">具体情况说明</th>
              <th rowspan="2">备注</th>
              <th rowspan="2">责任部门</th>
            </tr>
            <tr>
              <!-- 季度同比增减率子表头 -->
              <th>{{ title1[0] || currentYearQuarterHeader }}</th>
              <th>{{ title1[1] || lastYearQuarterHeader }}</th>
              <th>{{ title1[2] || yearOverYearHeader }}</th>

              <!-- 季度环比增减率子表头 -->
              <th>{{ title1[3] || lastQuarterHeader }}</th>
              <th>{{ title1[4] || currentQuarterAmountHeader }}</th>
              <th>{{ title1[5] || twoQuartersAgoHeader }}</th>
              <th>{{ title1[6] || lastQuarterAmountHeader }}</th>
              <th>{{ title1[7] || quarterOverQuarterHeader }}</th>
            </tr>
            <!-- <tr class="formula-header">
            <th>E5</th>
            <th>F5</th>
            <th>G5</th>
            <th>H5</th>
            <th>I5</th>
            <th>J5</th>
            <th>K5</th>
            <th>L5</th>
          </tr> -->
          </thead>

          <!-- 表格内容 -->
          <tbody>
            <tr>
              <td></td>
              <td></td>
              <td colspan="2" class="color-blue">填报说明：</td>
              <td>
                <div class="color-red">
                  {{
                    title2.key1 ||
                    `①标红指标 按区间值填列，填报区间为： (${getCurrentYearStartDate()}-${getCurrentYearEndDate()})`
                  }}
                </div>
                <div class="color-purple">
                  {{
                    title2.key2 ||
                    `②未标色指标 按时点值填列，填截至${getCurrentYearEndDate()}数据`
                  }}
                </div>
              </td>
              <td>
                <div class="color-red">
                  {{
                    title2.key3 ||
                    `①标红指标 按区间值填列，填报区间为： (${getLastYearStartDate()}-${getLastYearEndDate()})`
                  }}
                </div>
                <div class="color-purple">
                  {{
                    title2.key4 ||
                    `②未标色指标 按时点值填列，填截至${getLastYearEndDate()}数据`
                  }}
                </div>
              </td>
              <td class="color-blue">计算公式： G5=(E5-F5)/F5</td>
              <td>
                <div class="color-red">
                  {{
                    title2.key5 ||
                    `①标红指标 按区间值填列，填报区间为： (${getCurrentYearStartDate()}-${getLastQuarterEndDate()})`
                  }}
                </div>
                <div class="color-purple">
                  {{
                    title2.key6 ||
                    `②未标色指标 按时点值填列，填截至${getLastQuarterEndDate()}数据`
                  }}
                </div>
              </td>
              <td class="color-blue">
                <div>计算公式： I5=(E5-H5)</div>
              </td>
              <td>
                <div class="color-red">
                  {{
                    title2.key7 ||
                    `①标红指标 按区间值填列，填报区间为： (${getCurrentYearStartDate()}-${getTwoQuartersAgoEndDate()})`
                  }}
                </div>
                <div class="color-purple">
                  {{
                    title2.key8 ||
                    `②未标色指标 按时点值填列，填截至${getTwoQuartersAgoEndDate()}数据`
                  }}
                </div>
              </td>
              <td class="color-blue">
                <div>计算公式： K5=(H5-J5)</div>
              </td>
              <td class="color-blue">
                <div>计算公式</div>
                <div class="color-blue">L5=(E5-H5)/H5</div>
              </td>
              <td class="color-blue">详尽描述指标数据的具体情况</td>
              <td></td>
              <td></td>
            </tr>
            <!-- 战略风险 -->
            <tr>
              <td rowspan="16" class="risk-group">战略风险</td>
              <td>宏观经济风险</td>
              <td colspan="2">国内外宏观经济形势变化</td>
              <td>{{ getDataByField('ZLSTRING1', 0) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 1) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 2) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 3) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 4) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 5) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 6) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 7) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 8) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 9) }}</td>
              <td>{{ getDataByField('ZLSTRING1', 10) }}</td>
            </tr>
            <tr>
              <td>政策风险</td>
              <td colspan="2">国家及行业政策变化</td>
              <td>{{ getDataByField('ZLSTRING2', 0) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 1) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 2) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 3) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 4) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 5) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 6) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 7) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 8) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 9) }}</td>
              <td>{{ getDataByField('ZLSTRING2', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="9">国际化经营风险</td>
              <td colspan="2">
                被美国列入"实体清单"、受关税政策影响的子企业数量（个）
              </td>
              <td>{{ getDataByField('ZLINTEGER1', 0) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 1) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 2) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 3) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 4) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 5) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 6) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 7) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 8) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 9) }}</td>
              <td>{{ getDataByField('ZLINTEGER1', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">境外中高风险地区境外资产总额</td>
              <td>金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL1', 10) }}</td>
            </tr>
            <tr>
              <td>占资产总额比重</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL2', 10) }}</td>
            </tr>
            <tr>
              <td colspan="2">境外重大建设项目逾期数量（个）</td>
              <td>{{ getDataByField('ZLINTEGER2', 0) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 1) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 2) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 3) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 4) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 5) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 6) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 7) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 8) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 9) }}</td>
              <td>{{ getDataByField('ZLINTEGER2', 10) }}</td>
            </tr>
            <tr>
              <td colspan="2" class="color-red">汇率损失金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL3', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">境外重大法律诉讼案件</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('ZLINTEGER3', 0) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 1) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 2) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 3) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 4) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 5) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 6) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 7) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 8) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 9) }}</td>
              <td>{{ getDataByField('ZLINTEGER3', 10) }}</td>
            </tr>
            <tr>
              <td>涉案金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL4', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">
                境外重大合规案件（如被世行禁止参与投标、境外腐败）
              </td>
              <td>数量（个）</td>
              <td>{{ getDataByField('ZLINTEGER4', 0) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 1) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 2) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 3) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 4) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 5) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 6) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 7) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 8) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 9) }}</td>
              <td>{{ getDataByField('ZLINTEGER4', 10) }}</td>
            </tr>
            <tr>
              <td>涉案金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL5', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">改革与业务转型风险</td>
              <td rowspan="2" class="color-red">主要业务板块收入</td>
              <td class="color-red">金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL6', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red">占总收入比重</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL7', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="3">科技创新风险</td>
              <td rowspan="2" class="color-red">研发投入</td>
              <td class="color-red">金额（万元）</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL8', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red">占总收入比重</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 0) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 1) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 2) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 3) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 4) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 5) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 6) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 7) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 8) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 9) }}</td>
              <td>{{ getDataByField('ZLBIGDECIMAL9', 10) }}</td>
            </tr>
            <tr>
              <td colspan="2">重大科技项目逾期数量（个）</td>
              <td>{{ getDataByField('ZLINTEGER5', 0) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 1) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 2) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 3) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 4) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 5) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 6) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 7) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 8) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 9) }}</td>
              <td>{{ getDataByField('ZLINTEGER5', 10) }}</td>
            </tr>
            <!-- 财务风险 -->
            <tr>
              <td rowspan="16" class="risk-group">财务风险</td>
              <td rowspan="11">金融及金融衍生品业务风险</td>
              <td colspan="2">衍生品盈亏金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL1', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">对外担保业务违约事项</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('CWINTEGER1', 0) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 1) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 2) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 3) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 4) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 5) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 6) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 7) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 8) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 9) }}</td>
              <td>{{ getDataByField('CWINTEGER1', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL2', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">融资租赁业务违约项目</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('CWINTEGER2', 0) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 1) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 2) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 3) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 4) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 5) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 6) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 7) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 8) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 9) }}</td>
              <td>{{ getDataByField('CWINTEGER2', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL3', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">债券、股票质押回购违约项目</td>
              <td>数量（笔）</td>
              <td>{{ getDataByField('CWINTEGER3', 0) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 1) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 2) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 3) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 4) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 5) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 6) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 7) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 8) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 9) }}</td>
              <td>{{ getDataByField('CWINTEGER3', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL4', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">信托业务违约项目</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('CWINTEGER4', 0) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 1) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 2) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 3) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 4) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 5) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 6) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 7) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 8) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 9) }}</td>
              <td>{{ getDataByField('CWINTEGER4', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL5', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">保理业务违约项目</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('CWINTEGER5', 0) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 1) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 2) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 3) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 4) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 5) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 6) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 7) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 8) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 9) }}</td>
              <td>{{ getDataByField('CWINTEGER5', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL6', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">债务风险</td>
              <td colspan="2">流动比率</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL7', 10) }}</td>
            </tr>
            <tr>
              <td colspan="2">资产负债率</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL8', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="3">现金流风险</td>
              <td class="color-red" colspan="2">现金流动负债比率</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL9', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">两金（应收账款和存货）总额</td>
              <td>金额（万元）</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL10', 10) }}</td>
            </tr>
            <tr>
              <td>占流动资产比重</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 0) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 1) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 2) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 3) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 4) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 5) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 6) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 7) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 8) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 9) }}</td>
              <td>{{ getDataByField('CWBIGDECIMAL11', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="8" class="risk-group">市场风险</td>
              <td rowspan="4">市场竞争风险</td>
              <td rowspan="2" class="color-red">主要产品价格下降</td>
              <td class="color-red">数量（个）</td>
              <td>{{ getDataByField('SCINTEGER1', 0) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 1) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 2) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 3) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 4) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 5) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 6) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 7) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 8) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 9) }}</td>
              <td>{{ getDataByField('SCINTEGER1', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red">最大降幅</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL1', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red" rowspan="2">主要产品市场占有率下降</td>
              <td class="color-red">数量（个）</td>
              <td>{{ getDataByField('SCINTEGER2', 0) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 1) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 2) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 3) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 4) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 5) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 6) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 7) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 8) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 9) }}</td>
              <td>{{ getDataByField('SCINTEGER2', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red">最大降幅</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL2', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="4">客户信用风险</td>
              <td rowspan="2">账龄三年及以上的应收账款</td>
              <td>金额（万元）</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL3', 10) }}</td>
            </tr>
            <tr>
              <td>占应收账款比重</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL4', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">逾期应收账款</td>
              <td>金额（万元）</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL5', 10) }}</td>
            </tr>
            <tr>
              <td>占应收账款比重</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 0) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 1) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 2) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 3) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 4) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 5) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 6) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 7) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 8) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 9) }}</td>
              <td>{{ getDataByField('SCBIGDECIMAL6', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="10" class="risk-group">运营风险</td>
              <td rowspan="2">经营效益风险</td>
              <td rowspan="2">亏损子企业</td>
              <td>数量（户）</td>
              <td>{{ getDataByField('YYINTEGER1', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER1', 10) }}</td>
            </tr>
            <tr>
              <td>金额（万元）</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 0) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 1) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 2) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 3) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 4) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 5) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 6) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 7) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 8) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 9) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL1', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="3" class="risk-group">投资风险</td>
              <td rowspan="2" class="color-red">非主业项目投资</td>
              <td class="color-red">金额（万元）</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 0) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 1) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 2) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 3) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 4) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 5) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 6) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 7) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 8) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 9) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL2', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red">占总投资额比重</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 0) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 1) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 2) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 3) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 4) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 5) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 6) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 7) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 8) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 9) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL3', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red" colspan="2">
                年度投资计划完成率
                <br />
                （季度实际投资额/全年计划投资额）
              </td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 0) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 1) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 2) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 3) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 4) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 5) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 6) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 7) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 8) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 9) }}</td>
              <td>{{ getDataByField('YYBIGDECIMAL4', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">安全、环保、质量风险</td>
              <td class="color-red" colspan="2">重大安全生产事故数量（个）</td>
              <td>{{ getDataByField('YYINTEGER2', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER2', 10) }}</td>
            </tr>
            <tr>
              <td class="color-red" colspan="2">
                重大及以上突发环境事件数量（个）
              </td>
              <td>{{ getDataByField('YYINTEGER3', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER3', 10) }}</td>
            </tr>
            <tr>
              <td>舆情风险</td>
              <td class="color-red" colspan="2">重大舆情事件数量（个）</td>
              <td>{{ getDataByField('YYINTEGER4', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER4', 10) }}</td>
            </tr>
            <tr>
              <td>采购与供应链管理风险</td>
              <td colspan="2">出现重要产品断供的子企业数量（个）</td>
              <td>{{ getDataByField('YYINTEGER5', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER5', 10) }}</td>
            </tr>
            <tr>
              <td>工程项目管理风险</td>
              <td colspan="2">境内重大建设项目逾期数量（个）</td>
              <td>{{ getDataByField('YYINTEGER6', 0) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 1) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 2) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 3) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 4) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 5) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 6) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 7) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 8) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 9) }}</td>
              <td>{{ getDataByField('YYINTEGER6', 10) }}</td>
            </tr>
            <tr>
              <td class="risk-group" rowspan="3">法律风险</td>
              <td rowspan="3">合规风险</td>
              <td colspan="2" class="color-red">重大监管处罚数量（个）</td>
              <td>{{ getDataByField('FLINTEGER1', 0) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 1) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 2) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 3) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 4) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 5) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 6) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 7) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 8) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 9) }}</td>
              <td>{{ getDataByField('FLINTEGER1', 10) }}</td>
            </tr>
            <tr>
              <td rowspan="2">境内重大法律诉讼案件</td>
              <td>数量（个）</td>
              <td>{{ getDataByField('FLINTEGER2', 0) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 1) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 2) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 3) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 4) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 5) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 6) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 7) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 8) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 9) }}</td>
              <td>{{ getDataByField('FLINTEGER2', 10) }}</td>
            </tr>
            <tr>
              <td>涉案金额（万元）</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 0) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 1) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 2) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 3) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 4) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 5) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 6) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 7) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 8) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 9) }}</td>
              <td>{{ getDataByField('FLBIGDECIMAL1', 10) }}</td>
            </tr>
            <tr>
              <td class="risk-group">其他风险</td>
              <td colspan="3">其他对企业经营发展造成重大影响的风险</td>
              <td>{{ getDataByField('QTSTRING1', 0) }}</td>
              <td>{{ getDataByField('QTSTRING1', 1) }}</td>
              <td>{{ getDataByField('QTSTRING1', 2) }}</td>
              <td>{{ getDataByField('QTSTRING1', 3) }}</td>
              <td>{{ getDataByField('QTSTRING1', 4) }}</td>
              <td>{{ getDataByField('QTSTRING1', 5) }}</td>
              <td>{{ getDataByField('QTSTRING1', 6) }}</td>
              <td>{{ getDataByField('QTSTRING1', 7) }}</td>
              <td>{{ getDataByField('QTSTRING1', 8) }}</td>
              <td>{{ getDataByField('QTSTRING1', 9) }}</td>
              <td>{{ getDataByField('QTSTRING1', 10) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 表格说明 -->
      <div class="table-notes">
        <!-- <h3>填报说明：</h3>
      <p>①标红指标按区间值填列，填报区间为：(2024.1.1-2024.9.30)</p>
      <p>②未标色指标按时点值填列，填截至2024.9.30数据</p> -->

        <h3>指标说明：</h3>
        <p>
          1.
          <strong>重大建设项目</strong>
          是指境内项目投资额在5000万元人民币以上（含）、境外项目投资额在1亿美元以上（含）的项目。
        </p>
        <p>
          2.
          <strong>主要业务板块</strong>
          是指本单位总收入排名前5位的业务板块，主要产品是指对本单位利润贡献排名前5位的产品。
        </p>
        <p>
          4.
          <strong>重大科技项目</strong>
          是指通过国家认定的重大科技项目。
        </p>
        <p>
          5.
          <strong>衍生品盈亏</strong>
          =本年度已实现盈亏+浮动盈亏；对外担保业务违约金额包括本金、利息及其他费用等资金代偿；融资租赁只统计融出业务；信托业务包括信托投资和设立信托业务。
        </p>
        <p>
          6.
          <strong>重大案件</strong>
          是指涉案金额5000万元人民币以上（含）的案件。
        </p>
        <p>
          7.
          <strong>重大监管处罚</strong>
          是指被司法机关或监管机构立案调查，主要资产被查封、扣押、冻结或企业面临行政处罚，对企业正常生产经营造成重大影响（涉案金额或勒令停产损失超过5000万元人民币）。
        </p>
        <p>
          8.
          <strong>重大舆情事件</strong>
          是指被境内或境外媒体网络刊载，造成重大负面舆情影响的事件。
        </p>
      </div>
    </div>
    <select-company
      ref="companyTree"
      @handleChooseCompany="handleChooseCompany"
    />
  </div>
</template>

<script>
  import { exportExcel, getHzList } from '@/api/risk/monitoringFill'
  import filterSearch from '@/components/filterSearch.vue'
  import selectCompany from '@/views/internal/internalTest/components/ChooseCompanys.vue'

  export default {
    name: 'RiskIndicatorSummarizationTab1',
    components: {
      filterSearch,
      selectCompany,
    },
    data() {
      return {
        quarterOptions: [
          { value: '一季度', label: '一季度' },
          { value: '二季度', label: '二季度' },
          { value: '三季度', label: '三季度' },
          { value: '四季度', label: '四季度' },
        ],
        localKey: 'risk-riskIndicatorSummarization-search',
        tableKey: 'risk-riskIndicatorSummarization-list',
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        searchMore: true,

        queryForm: {
          jd: (() => {
            const month = new Date().getMonth() + 1
            if (month >= 1 && month <= 3) return '一季度'
            if (month >= 4 && month <= 6) return '二季度'
            if (month >= 7 && month <= 9) return '三季度'
            return '四季度'
          })(),
          year: new Date().getFullYear().toString(),
          orgid: '1000',
          companyName: '',
        },
        reportingUnit: '',
        reportingDate: '',
        reporter: '',
        contact: '',
        riskData: [],
        title1: [],
        title2: [],
      }
    },
    computed: {
      // 动态标题
      dynamicTitle() {
        return `${this.reportingUnit || ''}${
          this.queryForm.year
        }年第${this.getQuarterNumber(
          this.queryForm.jd
        )}季度风险分类监测指标体系表`
      },
      // 当前年季度末表头
      currentYearQuarterHeader() {
        return `截至${this.queryForm.year}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度末E5`
      },
      // 去年同期表头
      lastYearQuarterHeader() {
        return `截至${this.queryForm.year - 1}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度末F5`
      },
      // 同比增减率表头
      yearOverYearHeader() {
        return `${this.queryForm.year}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度同比增减率G5`
      },
      // 上季度末表头
      lastQuarterHeader() {
        const lastQuarter = this.getPreviousQuarter(this.queryForm.jd)
        const year =
          this.queryForm.jd === '一季度'
            ? this.queryForm.year - 1
            : this.queryForm.year
        return `截至${year}年${this.getQuarterNumber(lastQuarter)}季度末H5`
      },
      // 当前季度产生额表头
      currentQuarterAmountHeader() {
        return `${this.queryForm.year}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度产生额I5`
      },
      // 上上季度末表头
      twoQuartersAgoHeader() {
        const twoQuartersAgo = this.getTwoQuartersAgo(this.queryForm.jd)
        let year = this.queryForm.year
        if (this.queryForm.jd === '一季度' || this.queryForm.jd === '二季度') {
          year = this.queryForm.year - 1
        }
        return `截至${year}年${this.getQuarterNumber(twoQuartersAgo)}季度末J5`
      },
      // 上季度产生额表头
      lastQuarterAmountHeader() {
        const lastQuarter = this.getPreviousQuarter(this.queryForm.jd)
        const year =
          this.queryForm.jd === '一季度'
            ? this.queryForm.year - 1
            : this.queryForm.year
        return `${year}年${this.getQuarterNumber(lastQuarter)}季度产生额K5`
      },
      // 环比增减率表头
      quarterOverQuarterHeader() {
        return `${this.queryForm.year}年${this.getQuarterNumber(
          this.queryForm.jd
        )}季度环比增减率L5`
      },
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.queryForm.companyName = userInfo.linkOrg.orgname
      this.queryForm.orgid = userInfo.linkOrg.orgid
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async fetchData() {
        // let params = {
        //   year: this.queryForm.year,
        //   jd: this.queryForm.jd,
        //   orgid: userInfo.linkOrg.orgid,
        //   companyName: userInfo.linkOrg.orgname,
        // }
        const { code, data, msg } = await getHzList(this.queryForm)
        this.riskData = data.result || []
        this.reportingUnit = data.orgName
        this.reporter = data.staffName
        this.reportingDate = data.time
        this.title1 = data.title1
        this.title2 = data.title2
      },
      // 获取当前季度
      getCurrentQuarter() {
        const month = new Date().getMonth() + 1
        if (month >= 1 && month <= 3) return '一季度'
        if (month >= 4 && month <= 6) return '二季度'
        if (month >= 7 && month <= 9) return '三季度'
        return '四季度'
      },
      // 将季度文字转换为数字
      getQuarterNumber(quarter) {
        const quarterMap = {
          一季度: '一',
          二季度: '二',
          三季度: '三',
          四季度: '四',
        }
        return quarterMap[quarter] || '一'
      },
      // 获取上一季度
      getPreviousQuarter(currentQuarter) {
        const quarterMap = {
          一季度: '四季度',
          二季度: '一季度',
          三季度: '二季度',
          四季度: '三季度',
        }
        return quarterMap[currentQuarter] || '四季度'
      },
      // 获取上上季度
      getTwoQuartersAgo(currentQuarter) {
        const quarterMap = {
          一季度: '三季度',
          二季度: '四季度',
          三季度: '一季度',
          四季度: '二季度',
        }
        return quarterMap[currentQuarter] || '三季度'
      },
      // 根据字段名和列索引获取数据
      getDataByField(fieldName, columnIndex) {
        if (!this.riskData || !this.riskData[columnIndex]) {
          return ''
        }
        return this.riskData[columnIndex][fieldName] || ''
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '公司', key: 'companyName' },
          { name: '季度', key: 'jd' },
          { name: '年度', key: 'year' },
        ]
        return fields
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        let params = {
          year: this.queryForm.year,
          jd: this.queryForm.jd,
          orgid: this.queryForm.orgid,
        }
        const data = await exportExcel(params)
        let fileName = `风险监测指标汇总.xls`
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },

      // 获取当前年度当前季度结束日期
      getCurrentYearEndDate() {
        const year = parseInt(this.queryForm.year)
        const quarter = this.queryForm.jd
        return this.getQuarterEndDate(year, quarter)
      },

      // 获取当前年度开始日期
      getCurrentYearStartDate() {
        const year = parseInt(this.queryForm.year)
        return `${year}.1.1`
      },

      // 获取上一年度同期结束日期
      getLastYearEndDate() {
        const year = parseInt(this.queryForm.year) - 1
        const quarter = this.queryForm.jd
        return this.getQuarterEndDate(year, quarter)
      },

      // 获取上一年度开始日期
      getLastYearStartDate() {
        const year = parseInt(this.queryForm.year) - 1
        return `${year}.1.1`
      },

      // 获取上一季度结束日期
      getLastQuarterEndDate() {
        const quarter = this.getPreviousQuarter(this.queryForm.jd)
        let year = parseInt(this.queryForm.year)
        if (this.queryForm.jd === '一季度') {
          year = year - 1
        }
        return this.getQuarterEndDate(year, quarter)
      },

      // 获取上上季度结束日期
      getTwoQuartersAgoEndDate() {
        const quarter = this.getTwoQuartersAgo(this.queryForm.jd)
        let year = parseInt(this.queryForm.year)
        if (this.queryForm.jd === '一季度' || this.queryForm.jd === '二季度') {
          year = year - 1
        }
        return this.getQuarterEndDate(year, quarter)
      },

      // 计算季度的结束日期
      getQuarterEndDate(year, quarter) {
        const quarterEndDates = {
          一季度: `${year}.3.31`,
          二季度: `${year}.6.30`,
          三季度: `${year}.9.30`,
          四季度: `${year}.12.31`,
        }
        return quarterEndDates[quarter] || `${year}.12.31`
      },

      // 重置搜索条件
      resetSearch() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.queryForm.companyName = userInfo.linkOrg.orgname
        this.queryForm.orgid = userInfo.linkOrg.orgid
        this.queryForm.jd = this.getCurrentQuarter()
        this.queryForm.year = new Date().getFullYear().toString()
        this.fetchData()
      },
      handleChooseCompany(node) {
        this.queryForm.companyName = node.label
        this.queryForm.orgid = node.id
      },
    },
  }
</script>

<style scoped>
  .risk-container {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
    color: #333;
    margin: 0 auto;
    background-color: #fff;
  }

  .table-header {
    text-align: center;
    margin-bottom: 25px;
    border-bottom: 2px solid #999;
    padding-bottom: 15px;
  }

  .table-header h2 {
    margin-bottom: 10px;
    font-size: 22px;
  }

  .reporting-info {
    display: flex;
    justify-content: center;
    gap: 20px;
    font-size: 14px;
  }

  .risk-table-container {
    overflow-x: auto;
    margin-bottom: 30px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    padding: 10px;
  }

  .risk-table {
    width: 100%;
    border-collapse: collapse;
    min-width: 1200px;
  }

  .risk-table th,
  .risk-table td {
    border: 1px solid #dee2e6;
    padding: 12px 10px;
    text-align: center;
    font-size: 13px;
    line-height: 1.5;
    min-width: 120px;
    max-width: 120px;
    word-wrap: break-word;
    word-break: break-all;
  }

  .risk-table th {
    font-weight: bold;
  }

  .formula-header th {
    font-size: 12px;
  }
  .risk-group {
    font-weight: bold;
  }

  .negative {
    color: #e5210b;
  }

  .percent {
    font-weight: bold;
  }

  .table-notes {
    padding: 20px;
    border-radius: 4px;
    border-left: 4px solid #776e6e;
  }

  .table-notes h3 {
    margin: 15px 0 10px;
    font-size: 16px;
  }

  .table-notes p {
    margin: 5px 0;
    font-size: 13px;
    line-height: 1.6;
  }

  .color-red {
    color: #e5210b;
  }
  .color-blue {
    color: #11b2cb;
  }
  .color-purple {
    color: #66669d;
  }
</style>
