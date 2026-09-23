<template>
  <div class="loading">
    <div class="loading-text">{{ loadintText || '' }}</div>
    <span class="dot"></span>
    <span class="dot"></span>
  </div>
</template>
<script>
export default {
  props: ['loadintText'],
  data() {
    return {
    }
  }
}
</script>
<style scoped>
.loading {
  /* display: flex;
  justify-content: center;
  align-items: center; */
  position: relative;
}

/* 定义黑点的样式 */
.dot {
  background-color: #333;
  left: 50px;
  position: absolute;
  top: 3px;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.dot:first-child {
  animation: moveRightToLeft 0.8s infinite ease-in-out both;
}
.dot:last-child {
  animation: moveLeftToRight 0.8s infinite ease-in-out both;
}

/* 定义动画效果 */
@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1.0);
  }
}

/* 定义动画效果 */
@keyframes moveRightToLeft {
  0% {
    opacity: 0.2;
    transform: translateX(0px) scale(0.6);
  }
  48% {
    opacity: 0;
    transform: translateX(-4.5px) scale(0.3);
  }
  52% {
    opacity: 0;
    transform: translateX(-4.5px) scale(0.3);
  }
  100% {
    opacity: 1;
    transform: translateX(0px) scale(0.6);
  }
}
/* 定义动画效果 */
@keyframes moveLeftToRight {
  0% {
    opacity: 1;
    transform: translateX(0px) scale(0.6);
  }
  48% {
    opacity: 0.5;
    transform: translateX(4.5px) scale(0.9);
  }
  52% {
    opacity: 0.5;
    transform: translateX(4.5px) scale(0.9);
  }
  100% {
    opacity: 0.2;
    transform: translateX(12px) scale(0.6);
  }
}
</style>