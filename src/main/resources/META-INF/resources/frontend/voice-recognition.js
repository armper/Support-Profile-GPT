import { LitElement, html, css } from 'lit';

class VoiceRecognition extends LitElement {
  static get styles() {
    return css`
      :host {
        display: block;
      }
    `;
  }

  static get properties() {
    return {
      continuous: { type: Boolean },
      speech: { type: String },
    };
  }

  constructor() {
    super();
    this.initializeRecognition();
    this.speech = `Press the 'Start' button to start recording your voice.`;
  }

  connectedCallback() {
    super.connectedCallback();
    this.initializeAttributes();
    this.initializeEventListeners();
  }

  initializeRecognition() {
    const SpeechRecognition =
      window.SpeechRecognition ||
      window.webkitSpeechRecognition ||
      window.mozSpeechRecognition ||
      window.msSpeechRecognition ||
      window.oSpeechRecognition;

    this.recognition =
      SpeechRecognition !== undefined
        ? new SpeechRecognition()
        : console.error('Your browser does not support the Web Speech API');
  }

  initializeAttributes() {
    this.continuous = this.hasAttribute('continuous');
    this.speech = this.getAttribute('speech') || '';

    if (this.recognition) {
      this.recognition.continuous = this.continuous;
      this.recognition.interimResults = false;
    }
  }

  initializeEventListeners() {
    if (!this.recognition) {
      console.log('recognition not initialized');
      return;
    }

    ['start', 'end', 'error', 'speechResult'].forEach((eventName) =>
      this.recognition.addEventListener(eventName, (e) =>
        this.dispatchEvent(new CustomEvent(eventName, { detail: e }))
      )
    );

    this.recognition.addEventListener('result', (e) => {
      this.speech = [...Array(e.results.length).keys()]
        .slice(e.resultIndex)
        .map((i) => e.results[i][0].transcript)
        .reduce((a, b) => a + b, this.speech);

      this.dispatchEvent(new CustomEvent('speechResult', { detail: { speech: this.speech } }));
    });
  }

  start() {
    this.recognition.start();
  }

  stop() {
    this.recognition.stop();
  }

  abort() {
    this.recognition.abort();
  }

  render() {
    return html`
      <slot></slot>
      <p>${this.speech}</p>
    `;
  }
}

customElements.define('voice-recognition', VoiceRecognition);
