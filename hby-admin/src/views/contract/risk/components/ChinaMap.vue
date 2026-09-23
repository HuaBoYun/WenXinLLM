<template>
  <el-card shadow="hover">
    <template #header>
      <vab-icon icon="road-map-line" />
      已回款及未回款区域分布
    </template>
    <vab-chart
      :init-options="initOptions"
      :option="option"
      style="height: 262px"
      theme="vab-echarts-theme"
    />
  </el-card>
</template>

<script>
  import axios from 'axios'
  import VabChart from '@/extra/VabChart'
  import _ from 'lodash'

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        countConfig: {
          startVal: 0,
          endVal: _.random(1000, 20000),
          decimals: 0,
          prefix: '',
          suffix: '',
          separator: ',',
          duration: 8000,
        },
        initOptions: {
          renderer: 'svg',
        },
        // 中国地图
        option: {},
      }
    },
    created() {
      this.getMap()
    },
    methods: {
      //获取数据
      async getMap() {
        const { data } = await axios({
          url: 'https://cdn.jsdelivr.net/npm/echarts@4.9.0/map/json/china.json',
          method: 'get',
        })
        VabChart.registerMap('china', data)
        this.option = {
          title: {
            text: '',
            subtext: '',
          },
          tooltip: {
            trigger: 'item',
          },
          dataRange: {
            min: 0,
            max: 55000,
            text: ['高', '低'],
            splitNumber: 0,
          },
          series: [
            {
              name: '2099年全国GDP分布',
              type: 'map',
              roam: false,
              map: 'china',
              selectedMode: 'multiple',
              emphasis: {
                label: {
                  show: true,
                },
              },
              data: [
                { name: '西藏', value: 0 },
                { name: '青海', value: 0 },
                { name: '宁夏', value: 0 },
                { name: '海南', value: 0 },
                { name: '甘肃', value: 0 },
                { name: '贵州', value: 0 },
                { name: '新疆', value: 0 },
                { name: '云南', value: 0 },
                { name: '重庆', value: 0 },
                { name: '吉林', value: 0 },
                { name: '山西', value: 0 },
                { name: '天津', value: 0 },
                { name: '江西', value: 0 },
                { name: '广西', value: 0 },
                { name: '陕西', value: 0 },
                { name: '黑龙江', value: 0 },
                { name: '内蒙古', value: 0 },
                { name: '安徽', value: 0 },
                { name: '北京', value: 0 },
                { name: '福建', value: 0 },
                { name: '上海', value: 0 },
                { name: '湖北', value: 0 },
                { name: '湖南', value: 0 },
                { name: '四川', value: 0 },
                { name: '辽宁', value: 0 },
                { name: '河北', value: 0 },
                { name: '河南', value: 0 },
                { name: '浙江', value: 0 },
                // { name: '山东', value: 0, selected: true },
                { name: '山东', value: 0 },
                { name: '江苏', value: 0 },
                { name: '广东', value: 0 },
              ],
            },
          ],
        }
      },
    },
  }
</script>
