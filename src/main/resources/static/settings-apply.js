(function(){
    const settings=JSON.parse(localStorage.getItem('siteSettings')||'{}');
    const theme=settings.theme||localStorage.getItem('theme')||'dark';
    const fontSize=settings.fontSize||14;
    const compact=settings.compact||false;

    // Theme apply — turant
    document.documentElement.setAttribute('data-theme',theme);

    // Font size
    document.documentElement.style.setProperty('--font-size', fontSize+'px');

    // Compact
    if(compact){
        const style=document.createElement('style');
        style.textContent='.main{padding:1rem!important}.s-card{padding:0.8rem!important}.welcome{padding:1.2rem!important}';
        document.head.appendChild(style);
    }

    // Language
    const lang=settings.lang||localStorage.getItem('lang')||'en';
    const t={
        en:{logout:'Logout',settings:'⚙️ Settings',chat:'💬 Open Chat',profile:'👤 Profile',changePassword:'🔐 Change Password',save:'💾 Save',allUsers:'All Users',searchUsers:'Search users...',totalUsers:'Total Users',welcome:'Welcome'},
        hi:{logout:'लॉगआउट',settings:'⚙️ सेटिंग्स',chat:'💬 चैट',profile:'👤 प्रोफ़ाइल',changePassword:'🔐 पासवर्ड बदलें',save:'💾 सहेजें',allUsers:'सभी उपयोगकर्ता',searchUsers:'खोजें...',totalUsers:'कुल उपयोगकर्ता',welcome:'स्वागत है'},
        es:{logout:'Cerrar sesión',settings:'⚙️ Configuración',chat:'💬 Chat',profile:'👤 Perfil',changePassword:'🔐 Cambiar contraseña',save:'💾 Guardar',allUsers:'Todos usuarios',searchUsers:'Buscar...',totalUsers:'Total usuarios',welcome:'Bienvenido'},
        fr:{logout:'Déconnexion',settings:'⚙️ Paramètres',chat:'💬 Chat',profile:'👤 Profil',changePassword:'🔐 Changer MDP',save:'💾 Sauvegarder',allUsers:'Tous utilisateurs',searchUsers:'Rechercher...',totalUsers:'Total utilisateurs',welcome:'Bienvenue'},
        de:{logout:'Abmelden',settings:'⚙️ Einstellungen',chat:'💬 Chat',profile:'👤 Profil',changePassword:'🔐 Passwort',save:'💾 Speichern',allUsers:'Alle Benutzer',searchUsers:'Suchen...',totalUsers:'Alle Benutzer',welcome:'Willkommen'},
        ja:{logout:'ログアウト',settings:'⚙️ 設定',chat:'💬 チャット',profile:'👤 プロフィール',changePassword:'🔐 パスワード変更',save:'💾 保存',allUsers:'全ユーザー',searchUsers:'検索...',totalUsers:'総ユーザー数',welcome:'ようこそ'},
    };
    const tr=t[lang]||t.en;

    window.addEventListener('DOMContentLoaded',()=>{
        // data-i18n attribute wale elements translate karo
        document.querySelectorAll('[data-i18n]').forEach(el=>{
            const key=el.getAttribute('data-i18n');
            if(tr[key])el.textContent=tr[key];
        });
    });
})();