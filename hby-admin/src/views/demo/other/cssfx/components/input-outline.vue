<template>
  <div>
    <input placeholder="Input Outline" type="text" />
    <span class="bottom" />
    <span class="right" />
    <span class="top" />
    <span class="left" />
  </div>
</template>

<style scoped>
  div {
    position: relative;
  }

  input {
    width: 6.5em;
    padding: 0.35em 0.45em;
    font-family: inherit;
    font-size: inherit;
    color: white;
    background-color: hsl(236, 32%, 26%);
    border: 1px solid transparent;
    transition: background-color 0.3s ease-in-out;
  }

  input:focus {
    outline: none;
  }

  input::placeholder {
    color: hsla(0, 0%, 100%, 0.6);
  }

  span {
    position: absolute;
    background-color: #1890ff;
    transition: transform 0.5s ease;
  }

  .bottom,
  .top {
    right: 0;
    left: 0;
    height: 1px;
    transform: scaleX(0);
  }

  .left,
  .right {
    top: 0;
    bottom: 0;
    width: 1px;
    transform: scaleY(0);
  }

  .bottom {
    bottom: 0;
    transform-origin: bottom right;
  }

  input:focus ~ .bottom {
    transform: scaleX(1);
    transform-origin: bottom left;
  }

  .right {
    right: 0;
    transform-origin: top right;
  }

  input:focus ~ .right {
    transform: scaleY(1);
    transform-origin: bottom right;
  }

  .top {
    top: 0;
    transform-origin: top left;
  }

  input:focus ~ .top {
    transform: scaleX(1);
    transform-origin: top right;
  }

  .left {
    left: 0;
    transform-origin: bottom left;
  }

  input:focus ~ .left {
    transform: scaleY(1);
    transform-origin: top left;
  }
</style>
