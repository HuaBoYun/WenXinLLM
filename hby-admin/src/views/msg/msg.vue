<template>
  <div class="system-log-container">
    <wysx ref="wysx" show-title />
    <wddb ref="wddb" show-title />
    <wdcy ref="wdcy" show-title />
    <wdpj v-if="false" ref="wdpj" show-title />
    <gzfayj v-if="false" ref="gzfayj" show-title />
    <zbfnyj v-if="false" ref="zbfnyj" show-title />
    <mxyj v-if="false" ref="mxyj" show-title />
    <zpyj v-if="false" ref="zpyj" show-title />
    <gzyj v-if="false" ref="gzyj" show-title />
  </div>
</template>

<script>
  import wysx from '@/views/msg/components/wysx'
  import wdcy from '@/views/msg/components/wdcy'
  import wddb from '@/views/msg/components/wddb'
  import wdpj from '@/views/msg/components/wdpj'
  import gzfayj from '@/views/msg/components/gzfayj'
  import zbfnyj from '@/views/msg/components/zbfnyj'
  import mxyj from '@/views/msg/components/mxyj'
  import zpyj from '@/views/msg/components/zpyj'
  import gzyj from '@/views/msg/components/gzyj'

  export default {
    name: 'Msg',
    components: { wysx, wdcy, wddb, wdpj, gzfayj, zbfnyj, mxyj, zpyj, gzyj },
    data() {
      return {
        list: [],
      }
    },
    created() {},

    methods: {},
  }
</script>
