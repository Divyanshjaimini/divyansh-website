(function(){
    // Settings load karo
    const settings=JSON.parse(localStorage.getItem('siteSettings')||'{}');
    const theme=settings.theme||localStorage.getItem('theme')||'dark';
    const fontSize=parseInt(settings.fontSize||14);
    const compact=settings.compact||false;
    const lang=settings.lang||localStorage.getItem('lang')||'en';

    // THEMES CSS inject karo
    const themeCSS=document.createElement('style');
    themeCSS.textContent=`
        :root[data-theme="dark"]{--bg:#030308;--bg2:#0a0a14;--bg3:rgba(255,255,255,0.04);--border:rgba(255,255,255,0.07);--text:#fff;--text2:rgba(255,255,255,0.45);--card:rgba(255,255,255,0.025);--nav:rgba(3,3,8,0.9);--accent:#667eea}
        :root[data-theme="light"]{--bg:#f0f2ff;--bg2:#fff;--bg3:rgba(102,126,234,0.06);--border:rgba(102,126,234,0.12);--text:#1a1a2e;--text2:#666688;--card:#fff;--nav:rgba(240,242,255,0.95);--accent:#667eea}
        :root[data-theme="blue"]{--bg:#010b1a;--bg2:#021224;--bg3:rgba(0,150,255,0.06);--border:rgba(0,150,255,0.15);--text:#e0f0ff;--text2:rgba(224,240,255,0.5);--card:rgba(0,150,255,0.04);--nav:rgba(1,11,26,0.95);--accent:#0096ff}
        :root[data-theme="green"]{--bg:#010f08;--bg2:#021a0d;--bg3:rgba(0,200,100,0.06);--border:rgba(0,200,100,0.15);--text:#e0ffe8;--text2:rgba(224,255,232,0.5);--card:rgba(0,200,100,0.04);--nav:rgba(1,15,8,0.95);--accent:#00c864}
        :root[data-theme="purple"]{--bg:#08010f;--bg2:#12021a;--bg3:rgba(150,0,255,0.06);--border:rgba(150,0,255,0.15);--text:#f0e0ff;--text2:rgba(240,224,255,0.5);--card:rgba(150,0,255,0.04);--nav:rgba(8,1,15,0.95);--accent:#9600ff}
        :root[data-theme="sunset"]{--bg:#0f0500;--bg2:#1a0a00;--bg3:rgba(255,100,0,0.06);--border:rgba(255,100,0,0.15);--text:#fff0e0;--text2:rgba(255,240,224,0.5);--card:rgba(255,100,0,0.04);--nav:rgba(15,5,0,0.95);--accent:#ff6400}
        body{font-size:${fontSize}px!important}
        ${compact?`
        .main{padding:1rem!important}
        .s-card{padding:0.8rem!important}
        .welcome{padding:1.2rem!important}
        .p-card,.pw-card{padding:1.2rem!important}
        `:''}
    `;
    document.head.appendChild(themeCSS);

    // Theme apply — turant
    document.documentElement.setAttribute('data-theme',theme);

    // Translations
    const translations={
        en:{
            logout:'Logout',settings:'⚙️ Settings',chat:'💬 Open Chat',
            profile:'👤 Profile',changePassword:'🔐 Change Password',
            save:'💾 Save',allUsers:'All Users',searchUsers:'Search users...',
            totalUsers:'Total Users',welcome:'Welcome',home:'Home',
            about:'About',contact:'Contact',login:'Login',joinNow:'Join Now',
            members:'Members',visitors:'Visitors',free:'Free',online:'Online',
            memberSince:'Member Since',status:'Status',role:'Role',userId:'User ID',
            active:'Active',admins:'Admins',normalUsers:'Normal Users',
        },
        hi:{
            logout:'लॉगआउट',settings:'⚙️ सेटिंग्स',chat:'💬 चैट खोलें',
            profile:'👤 प्रोफ़ाइल',changePassword:'🔐 पासवर्ड बदलें',
            save:'💾 सहेजें',allUsers:'सभी उपयोगकर्ता',searchUsers:'खोजें...',
            totalUsers:'कुल उपयोगकर्ता',welcome:'स्वागत है',home:'होम',
            about:'हमारे बारे में',contact:'संपर्क',login:'लॉगिन',joinNow:'अभी जुड़ें',
            members:'सदस्य',visitors:'आगंतुक',free:'मुफ़्त',online:'ऑनलाइन',
            memberSince:'सदस्य बने',status:'स्थिति',role:'भूमिका',userId:'यूज़र आईडी',
            active:'सक्रिय',admins:'एडमिन',normalUsers:'सामान्य उपयोगकर्ता',
        },
        es:{
            logout:'Cerrar sesión',settings:'⚙️ Configuración',chat:'💬 Chat',
            profile:'👤 Perfil',changePassword:'🔐 Cambiar contraseña',
            save:'💾 Guardar',allUsers:'Todos usuarios',searchUsers:'Buscar...',
            totalUsers:'Total usuarios',welcome:'Bienvenido',home:'Inicio',
            about:'Acerca de',contact:'Contacto',login:'Iniciar sesión',joinNow:'Únete',
            members:'Miembros',visitors:'Visitantes',free:'Gratis',online:'En línea',
            memberSince:'Miembro desde',status:'Estado',role:'Rol',userId:'ID Usuario',
            active:'Activo',admins:'Admins',normalUsers:'Usuarios normales',
        },
        fr:{
            logout:'Déconnexion',settings:'⚙️ Paramètres',chat:'💬 Chat',
            profile:'👤 Profil',changePassword:'🔐 Changer MDP',
            save:'💾 Sauvegarder',allUsers:'Tous utilisateurs',searchUsers:'Rechercher...',
            totalUsers:'Total utilisateurs',welcome:'Bienvenue',home:'Accueil',
            about:'À propos',contact:'Contact',login:'Connexion',joinNow:'Rejoindre',
            members:'Membres',visitors:'Visiteurs',free:'Gratuit',online:'En ligne',
            memberSince:'Membre depuis',status:'Statut',role:'Rôle',userId:'ID Utilisateur',
            active:'Actif',admins:'Admins',normalUsers:'Utilisateurs normaux',
        },
        de:{
            logout:'Abmelden',settings:'⚙️ Einstellungen',chat:'💬 Chat',
            profile:'👤 Profil',changePassword:'🔐 Passwort ändern',
            save:'💾 Speichern',allUsers:'Alle Benutzer',searchUsers:'Suchen...',
            totalUsers:'Alle Benutzer',welcome:'Willkommen',home:'Startseite',
            about:'Über uns',contact:'Kontakt',login:'Anmelden',joinNow:'Beitreten',
            members:'Mitglieder',visitors:'Besucher',free:'Kostenlos',online:'Online',
            memberSince:'Mitglied seit',status:'Status',role:'Rolle',userId:'Benutzer-ID',
            active:'Aktiv',admins:'Admins',normalUsers:'Normale Benutzer',
        },
        ja:{
            logout:'ログアウト',settings:'⚙️ 設定',chat:'💬 チャット',
            profile:'👤 プロフィール',changePassword:'🔐 パスワード変更',
            save:'💾 保存',allUsers:'全ユーザー',searchUsers:'検索...',
            totalUsers:'総ユーザー数',welcome:'ようこそ',home:'ホーム',
            about:'について',contact:'お問い合わせ',login:'ログイン',joinNow:'参加',
            members:'メンバー',visitors:'訪問者',free:'無料',online:'オンライン',
            memberSince:'メンバー登録',status:'ステータス',role:'役割',userId:'ユーザーID',
            active:'アクティブ',admins:'管理者',normalUsers:'一般ユーザー',
        },
    };

    window._t=translations[lang]||translations.en;

    // DOM ready hone par translate karo
    const applyTranslations=()=>{
        document.querySelectorAll('[data-i18n]').forEach(el=>{
            const key=el.getAttribute('data-i18n');
            if(window._t[key]){
                if(el.tagName==='INPUT'){
                    el.placeholder=window._t[key];
                } else {
                    el.textContent=window._t[key];
                }
            }
        });
    };

    if(document.readyState==='loading'){
        document.addEventListener('DOMContentLoaded',applyTranslations);
    } else {
        applyTranslations();
    }
})();