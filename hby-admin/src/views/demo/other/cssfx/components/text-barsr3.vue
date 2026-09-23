<template>
  <span>Bars</span>
</template>

<style scoped>
  span {
    position: relative;
  }

  span::before,
  span::after {
    position: absolute;
    left: 50%;
    width: 100%;
    height: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.4s ease;
    transform: translateX(-50%) scaleX(0);
    transform-origin: center;
  }

  span::before {
    top: 0;
  }

  span::after {
    bottom: 0;
  }

  span:hover::before,
  span:hover::after {
    transform: translateX(-50%) scaleX(1);
  }
</style>
