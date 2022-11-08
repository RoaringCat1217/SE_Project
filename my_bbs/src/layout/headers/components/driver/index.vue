<template>
  <div id="guide" @click.prevent.stop="handleGuide">
    <svg-icon icon="guide"></svg-icon>
  </div>
</template>

<script setup>
import Driver from 'driver.js'
import 'driver.js/dist/driver.min.css'
import { onMounted } from 'vue'
import { steps } from './steps'
import { watchLang } from '@/i18n/watchlang'
import i18n from '@/i18n'
const t = i18n.global.t
let driver
onMounted(() => {
  initDriver()
})

const initDriver = () => {
  driver = new Driver({
    animate: false, // Whether to animate or not
    opacity: 0.75, // Background opacity (0 means only popovers and without overlay)
    padding: 10, // Distance of element from around the edges
    allowClose: true, // Whether the click on overlay should close or not
    overlayClickNext: false, // Whether the click on overlay should move next
    doneBtnText: t('driver.doneBtnText'), // Text on the final button
    closeBtnText: t('driver.closeBtnText'), // Text on the close button for this step
    stageBackground: '#ffffff', // Background color for the staged behind highlighted element
    nextBtnText: t('driver.nextBtnText'), // Next button text for this step
    prevBtnText: t('driver.prevBtnText') // Previous button text for this step
  })
}
watchLang(initDriver)

const handleGuide = () => {
  driver.defineSteps(steps(t))
  driver.start()
}
</script>

<style lang="scss" scoped></style>
