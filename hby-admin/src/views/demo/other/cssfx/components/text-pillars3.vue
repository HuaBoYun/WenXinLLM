<template>
  <span>Pillars</span>
</template>

<style scoped>
  span {
    position: relative;
  }

  span::before,
  span::after {
    position: absolute;
    top: 0;
    bottom: 0;
    width: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.5s ease;
    transform: scaleY(0);
    transform-origin: center;
  }

  span::before {
    left: -8px;
  }

  span::after {
    right: -8px;
  }

  span:hover::before,
  span:hover::after {
    transform: scaleY(1);
  }
</style>
