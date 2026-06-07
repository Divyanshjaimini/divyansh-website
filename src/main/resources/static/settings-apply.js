// Global Settings Apply — Har page par ye script chalegi
(function() {
    const settings = JSON.parse(localStorage.getItem('siteSettings') || '{}');
    const theme = settings.theme || localStorage.getItem('theme') || 'dark';
    const lang = settings.lang || localStorage.getItem('lang') || 'en';
    const fontSize = settings.fontSize || 14;
    const compact = settings.compact || false;

    // Theme apply
    document.documentElement.setAttribute('data-theme', theme);

    // Font size apply
    document.documentElement.style.fontSize = fontSize + 'px';

    // Compact mode
    if (compact) {
        document.documentElement.classList.add('compact');
    }

    // Language translations
    const translations = {
        en: {
            home: 'Home',
            about: 'About',
            contact: 'Contact',
            login: 'Login',
            joinNow: 'Join Now',
            welcome: 'Welcome',
            logout: 'Logout',
            settings: 'Settings',
            chat: 'Open Chat',
            profile: 'Profile',
            changePassword: 'Change Password',
            save: 'Save',
            totalUsers: 'Total Users',
            allUsers: 'All Users',
            searchUsers: 'Search users...',
        },
        hi: {
            home: 'होम',
            about: 'हमारे बारे में',
            contact: 'संपर्क',
            login: 'लॉगिन',
            joinNow: 'अभी जुड़ें',
            welcome: 'स्वागत है',
            logout: 'लॉगआउट',
            settings: 'सेटिंग्स',
            chat: 'चैट खोलें',
            profile: 'प्रोफ़ाइल',
            changePassword: 'पासवर्ड बदलें',
            save: 'सहेजें',
            totalUsers: 'कुल उपयोगकर्ता',
            allUsers: 'सभी उपयोगकर्ता',
            searchUsers: 'उपयोगकर्ता खोजें...',
        },
        es: {
            home: 'Inicio',
            about: 'Acerca de',
            contact: 'Contacto',
            login: 'Iniciar sesión',
            joinNow: 'Únete ahora',
            welcome: 'Bienvenido',
            logout: 'Cerrar sesión',
            settings: 'Configuración',
            chat: 'Abrir Chat',
            profile: 'Perfil',
            changePassword: 'Cambiar contraseña',
            save: 'Guardar',
            totalUsers: 'Total usuarios',
            allUsers: 'Todos los usuarios',
            searchUsers: 'Buscar usuarios...',
        },
        fr: {
            home: 'Accueil',
            about: 'À propos',
            contact: 'Contact',
            login: 'Connexion',
            joinNow: 'Rejoindre',
            welcome: 'Bienvenue',
            logout: 'Déconnexion',
            settings: 'Paramètres',
            chat: 'Ouvrir le chat',
            profile: 'Profil',
            changePassword: 'Changer mot de passe',
            save: 'Sauvegarder',
            totalUsers: 'Total utilisateurs',
            allUsers: 'Tous les utilisateurs',
            searchUsers: 'Rechercher...',
        },
        de: {
            home: 'Startseite',
            about: 'Über uns',
            contact: 'Kontakt',
            login: 'Anmelden',
            joinNow: 'Jetzt beitreten',
            welcome: 'Willkommen',
            logout: 'Abmelden',
            settings: 'Einstellungen',
            chat: 'Chat öffnen',
            profile: 'Profil',
            changePassword: 'Passwort ändern',
            save: 'Speichern',
            totalUsers: 'Alle Benutzer',
            allUsers: 'Alle Benutzer',
            searchUsers: 'Benutzer suchen...',
        },
        ja: {
            home: 'ホーム',
            about: '私たちについて',
            contact: 'お問い合わせ',
            login: 'ログイン',
            joinNow: '今すぐ参加',
            welcome: 'ようこそ',
            logout: 'ログアウト',
            settings: '設定',
            chat: 'チャットを開く',
            profile: 'プロフィール',
            changePassword: 'パスワード変更',
            save: '保存',
            totalUsers: '総ユーザー数',
            allUsers: '全ユーザー',
            searchUsers: 'ユーザー検索...',
        }
    };

    // Apply translations after page loads
    window.addEventListener('DOMContentLoaded', () => {
        const t = translations[lang] || translations.en;
        // data-lang attribute wale elements translate karo
        document.querySelectorAll('[data-lang]').forEach(el => {
            const key = el.getAttribute('data-lang');
            if (t[key]) el.textContent = t[key];
        });
    });
})();