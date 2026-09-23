<template>
  <div>
    <input placeholder="Input Underline" type="text" />
    <span />
  </div>
</template>

<style scoped>
  div {
    position: relative;
  }

  input {
    width: 6.5em;
    font-family: inherit;
    font-size: inherit;
    color: white;
    background-color: transparent;
    border: 1px solid transparent;
    border-bottom-color: hsla(185, 100%, 62%, 0.2);
  }

  input:focus {
    outline: none;
  }

  input::placeholder {
    color: hsla(0, 0%, 100%, 0.6);
  }

  span {
    position: absolute;
    right: 0;
    bottom: 0;
    left: 0;
    height: 1px;
    background-color: #1890ff;
    transition: transform 0.5s ease;
    transform: scaleX(0);
    transform-origin: bottom right;
  }

  input:focus ~ span {
    transform: scaleX(1);
    transform-origin: bottom left;
  }
</style>
