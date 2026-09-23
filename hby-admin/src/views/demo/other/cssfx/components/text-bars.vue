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
    right: 0;
    left: 0;
    height: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.5s ease;
    transform: scaleX(0);
    transform-origin: right center;
  }

  span::before {
    top: 0;
  }

  span::after {
    bottom: 0;
  }

  span:hover::before,
  span:hover::after {
    transform: scaleX(1);
    transform-origin: left center;
  }
</style>
