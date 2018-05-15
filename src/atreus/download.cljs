(ns atreus.download)

(defn download [filename content & [mime-type]]
  (let [mime-type (or mime-type (str "text/plain;charset=" (.-characterSet js/document)))
        blob (new js/Blob
                  #js [(str content)]
                  #js {:type mime-type})]
    (if (.-msSaveOrOpenBlob js/window.navigator)
      (.msSaveOrOpenBlob js/window.navigator blob filename)

      (let [el (.createElement js/window.document "a")
            obj-url (.createObjectURL js/window.URL blob)]
        (set! (.-href el) obj-url)
        (set! (.-download el) filename)
        (.appendChild js/document.body el)
        (.click el)
        (.removeChild js/document.body el)
        (.revokeObjectURL js/window.URL obj-url)))))
