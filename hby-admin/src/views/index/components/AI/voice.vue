<template>
  <i
    class="icon el-icon-video-pause mic mic-icon"
    v-if="isRec"
    @click="stop"
  ></i>
  <i class="icon el-icon-microphone mic mic-icon" v-else @click="onRecord"></i>
</template>

<script>
  import { WebSocketConnectMethod } from './util/wsconnecter.js'
  import Recorder from './util/recorder-core.js'

  export default {
    name: 'voice',
    data() {
      return {
        content: '',
        rec: null,
        wsconnecter: null,
        isRec: false,
        offline_text: '',
        rec_text: '',
        isfilemode: false,
        sampleBuf: new Int16Array(),
      }
    },
    props: {
      chatType: {
        // 总结
        type: String,
        default: 'chat', // summary === 'summary'-总结，chat-聊天，index-指标
      },
      height: {
        type: String,
        default: '400px',
      },
      fontSize: {
        type: String,
        default: '12px',
      },
    },
    created() {
      this.wsconnecter = new WebSocketConnectMethod({
        msgHandle: this.getJsonMessage,
        stateHandle: this.getConnState,
      })
      this.rec = Recorder({
        type: 'pcm',
        bitRate: 16,
        sampleRate: 16000,
        onProcess: this.recProcess,
      })
    },
    beforeDestroy() {
      console.log('beforeDestroy')
      this.stop()
    },
    methods: {
      clear() {
        this.rec_text = ''
        this.offline_text = ''
      },
      // 识别启动、停止、清空操作
      onRecord() {
        // 清除显示
        this.clear()
        //控件状态更新
        // console.log("this.isfilemode" + this.isfilemode);

        //启动连接
        var ret = this.wsconnecter.wsStart()
        // 1 is ok, 0 is error
        if (ret == 1) {
          // info_div.innerHTML = "正在连接asr服务器，请等待...";
          this.isRec = true
          return 1
        } else {
          return 0
        }
      },
      getJsonMessage(jsonMsg) {
        //console.log(jsonMsg);
        console.log('message: ' + JSON.parse(jsonMsg.data)['text'])
        var rectxt = '' + JSON.parse(jsonMsg.data)['text']
        var asrmodel = JSON.parse(jsonMsg.data)['mode']
        var is_final = JSON.parse(jsonMsg.data)['is_final']
        var timestamp = JSON.parse(jsonMsg.data)['timestamp']
        console.log('asrmodel', asrmodel)
        if (asrmodel == '2pass-offline' || asrmodel == 'offline') {
          this.offline_text =
            this.offline_text + this.handleWithTimestamp(rectxt, timestamp) //rectxt; //.replace(/ +/g,"");
          this.rec_text = this.offline_text
        } else {
          this.rec_text = this.rec_text + rectxt //.replace(/ +/g,"");
        }
        console.log('rectxt', this.rec_text)
        this.$emit('getMessage', this.rec_text)

        if (this.isfilemode == true && is_final == true) {
          console.log('call stop ws!')
          // play_file();
          this.wsconnecter.wsStop()

          // info_div.innerHTML = "请点击连接";

          // btnStart.disabled = true;
          // btnStop.disabled = true;
          // btnConnect.disabled = false;
        }
      },
      getConnState(connState) {
        if (connState === 0) {
          //on open
          this.recordStart()

          // info_div.innerHTML = '连接成功!请点击开始';
          // if (this.isfilemode == true) {
          //   info_div.innerHTML = '请耐心等待,大文件等待时间更长';
          //   start_file_send();
          // }
          // else {
          //   btnStart.disabled = false;
          //   btnStop.disabled = true;
          //   btnConnect.disabled = true;
          // }
        } else if (connState === 1) {
          //stop();
        } else if (connState === 2) {
          this.stop()
          console.log('connecttion error')

          console.log(
            '连接地址 wss://office.wenxin.example.com/asr/ws 失败,请检查asr地址和端口。或试试界面上手动授权，再连接。'
          )
          // btnStart.disabled = true;
          // btnStop.disabled = true;
          // btnConnect.disabled = false;

          // info_div.innerHTML = '请点击连接';
        }
      },
      handleWithTimestamp(tmptext, tmptime) {
        if (tmptime == null || tmptime == 'undefined' || tmptext.length <= 0) {
          return tmptext
        }
        tmptext = tmptext.replace(/。|？|，|、|\?|\.|\ /g, ',') // in case there are a lot of "。"
        var words = tmptext.split(',') // split to chinese sentence or english words
        var jsontime = JSON.parse(tmptime) //JSON.parse(tmptime.replace(/\]\]\[\[/g, "],[")); // in case there are a lot segments by VAD
        var char_index = 0 // index for timestamp
        var text_withtime = ''
        for (var i = 0; i < words.length; i++) {
          if (words[i] == 'undefined' || words[i].length <= 0) {
            continue
          }
          if (/^[a-zA-Z]+$/.test(words[i])) {
            // if it is english
            text_withtime =
              text_withtime +
              jsontime[char_index][0] / 1000 +
              ':' +
              words[i] +
              '\n'
            char_index = char_index + 1 //for english, timestamp unit is about a word
          } else {
            // if it is chinese
            text_withtime =
              text_withtime +
              jsontime[char_index][0] / 1000 +
              ':' +
              words[i] +
              '\n'
            char_index = char_index + words[i].length //for chinese, timestamp unit is about a char
          }
        }
        return text_withtime
      },
      stop() {
        if (!this.isRec) return
        var chunk_size = new Array(5, 10, 5)
        var request = {
          chunk_size: chunk_size,
          wav_name: 'h5',
          is_speaking: false,
          chunk_interval: 10,
          mode: '2pass',
        }
        console.log(request)
        // if (sampleBuf.length > 0) {
        //   wsconnecter.wsSend(sampleBuf);
        //   console.log("sampleBuf.length" + sampleBuf.length);
        //   sampleBuf = new Int16Array();
        // }
        this.wsconnecter.wsSend(JSON.stringify(request))
        // 控件状态更新

        this.isRec = false
        // info_div.innerHTML = "发送完数据,请等候,正在识别...";
        console.log('发送完数据,请等候,正在识别...')

        if (this.isfilemode == false) {
          // btnStop.disabled = true;
          // btnStart.disabled = true;
          // btnConnect.disabled = true;
          //wait 3s for asr result
          setTimeout(function () {
            console.log('call stop ws!')
            // wsconnecter.wsStop();
            // btnConnect.disabled = false;
            // info_div.innerHTML = "请点击连接";
          }, 3000)

          this.rec.stop(
            function (blob, duration) {
              console.log('rec.stop')
              // var audioBlob = Recorder.pcm2wav(data = { sampleRate: 16000, bitRate: 16, blob: blob },
              //   function (theblob, duration) {
              //     console.log(theblob);
              //     var audio_record = document.getElementById('audio_record');
              //     audio_record.src = (window.URL || webkitURL).createObjectURL(theblob);
              //     audio_record.controls = true;
              //     //audio_record.play();

              //   }, function (msg) {
              //     console.log(msg);
              //   }
              // );
            },
            function (errMsg) {
              console.log('errMsg: ' + errMsg)
            }
          )
        }
      },
      recProcess(
        buffer,
        powerLevel,
        bufferDuration,
        bufferSampleRate,
        newBufferIdx,
        asyncEnd
      ) {
        if (this.isRec === true) {
          var data_48k = buffer[buffer.length - 1]

          var array_48k = new Array(data_48k)
          var data_16k = Recorder.SampleData(
            array_48k,
            bufferSampleRate,
            16000
          ).data

          this.sampleBuf = Int16Array.from([...this.sampleBuf, ...data_16k])
          var chunk_size = 960 // for asr chunk_size [5, 10, 5]
          // info_div.innerHTML = "" + bufferDuration / 1000 + "s";
          while (this.sampleBuf.length >= chunk_size) {
            var sendBuf = this.sampleBuf.slice(0, chunk_size)
            this.sampleBuf = this.sampleBuf.slice(
              chunk_size,
              this.sampleBuf.length
            )
            this.wsconnecter.wsSend(sendBuf)
          }
        }
      },
      recordStart() {
        this.rec.open(() => {
          this.rec.start()
          console.log('开始')
          // btnStart.disabled = true;
          // btnStop.disabled = false;
          // btnConnect.disabled = true;
        })
      },
    },
  }
</script>

<style scoped lang="scss">
  .mic {
    cursor: pointer;

    .mic-icon {
      width: 100%;
      height: 100%;
    }
  }
</style>
