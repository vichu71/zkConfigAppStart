CKEDITOR.plugins.addExternal('wordcount', 'plugins/wordcount/', 'plugin.js');
CKEDITOR.plugins.addExternal('notification', 'plugins/notification/', 'plugin.js');

CKEDITOR.editorConfig = function (config) {
    config.extraPlugins = 'wordcount,notification';
    config.language = 'en';
    config.toolbar = 'custom';
    config.toolbar_custom = [
        '/',
        {
            name: 'basicstyles',
            items: ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat']
        },
        {
            name: 'paragraph',
            items: ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']
        },
        {
            name: 'links',
            items: ['Link', 'Unlink']
        },
        {
            name: 'insert',
            items: ['Image', 'Table']
        }
    ];
    config.wordcount = {
        showParagraphs: true,
        showWordCount: true,
        showCharCount: true,
        countSpacesAsChars: true,
        countHTML: false,
        countLineBreaks: false,
        maxWordCount: 1000,
        maxCharCount: -1,
        pasteWarningDuration: 0
    };
};