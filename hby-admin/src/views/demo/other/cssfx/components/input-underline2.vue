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
    border-bottom-color: hsla(341, 97%, 59%, 0.2);
  }

  input:focus {
    outline: none;
  }

  input::placeholder {
    color: hsla(0, 0%, 100%, 0.6);
  }

  span {
    position: absolute;
    bottom: 0;
    left: 50%;
    width: 100%;
    height: 1px;
    background-color: #1890ff;
    opacity: 0;
    transition: all 0.3s ease;
    transform: translate(-50%, 0) scaleX(0);
    transform-origin: center;
  }

  input:focus ~ span {
    opacity: 1;
    transform: translate(-50%, 0) scaleX(1);
  }
</style>
