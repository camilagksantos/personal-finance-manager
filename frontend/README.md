# Personal Finance Manager — Frontend

Modern Angular frontend for managing personal finances. Built with Angular 21, Signals, and Angular Material.

## ✨ Features

- Standalone components — zero NgModules
- Zoneless execution with Angular Signals
- Time-based light/dark theme switching
- Glassmorphism UI with Angular Material M3
- Interactive charts with Chart.js
- Full CRUD for accounts, categories and transactions
- Route guards and localStorage persistence
- 22 passing unit tests with Vitest

## 🏗 Architecture Overview
```
src/app/
├── core/
│   ├── models/        ← TypeScript interfaces aligned with backend DTOs
│   ├── services/      ← Signal-based HTTP services
│   ├── guards/        ← Route protection
│   └── interceptors/
│
├── shared/            ← Reusable components and utilities
│
├── features/
│   ├── dashboard/     ← KPIs, charts and recent activity
│   ├── accounts/      ← Full CRUD with filters and sorting
│   ├── transactions/  ← Full CRUD with chip-based filters
│   ├── categories/    ← Full CRUD
│   └── reports/       ← Pending backend implementation
│
└── layout/
    ├── header/        ← Theme toggle and sidebar control
    ├── sidebar/       ← Glassmorphism navigation
    └── main-layout/   ← Application shell
```

## 🚀 Getting Started

### Prerequisites

- Node.js v22.22.1+
- Angular CLI 21+
- Backend running on `http://localhost:8080`

### Install dependencies
```bash
cd frontend
npm install
```

### Run development server
```bash
ng serve
```

Open `http://localhost:4200` in your browser.

> The backend must be running for data to load. See the backend README for setup instructions.

## 🧪 Tests

Run all unit tests:
```bash
ng test
```

| Layer | Description |
|-------|-------------|
| Services | Signal state, HTTP calls, race condition guards |
| Components | Dashboard KPIs, overlay, recent transactions |
| Guards | Auth redirect when no user selected |
| Dialogs | MatDialogRef and MAT_DIALOG_DATA providers |

## 🛢 State Management

No external state management library. State is handled entirely with Angular Signals:

- `signal()` for mutable state
- `computed()` for derived state (KPIs, filters, totals)
- `effect()` for side effects (loading transactions when accounts change)

## 🎨 Theming

Automatic theme switching based on time of day:

- Light theme → 06:00–18:00
- Dark theme → 18:00–06:00

Manual override available via toggle in the header.

## 📊 Charts

Dashboard includes three Chart.js visualisations:

- Receitas vs Despesas (bar chart by month)
- Despesas por categoria (doughnut chart)
- Evolução do saldo (line chart)

All charts adapt colours reactively when the theme changes.

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Framework | Angular 21.2.4 |
| Reactivity | Angular Signals (zoneless) |
| UI | Angular Material M3 |
| Charts | Chart.js 4.5.1 + ng2-charts |
| Date handling | date-fns 4.1.0 |
| Testing | Vitest 4.1.0 |
| Runtime | Node.js 22.22.1 |

## ⚙️ Design Decisions

**Why Signals instead of RxJS for state?**
Signals provide fine-grained reactivity without the overhead of Zone.js. In a zoneless application, Signals are the native change detection mechanism — components update only when the specific Signal they read actually changes, not on every async event.

**Why no NgModules?**
Angular 21 defaults to standalone components. Removing NgModules reduces boilerplate, improves tree-shaking and makes each component's dependencies explicit in its own `imports` array.

**Why localStorage for user persistence?**
The application has no authentication layer — users come from an external API (JSONPlaceholder). Persisting the selected user in localStorage avoids forcing the user to re-select on every page refresh while keeping the implementation simple and framework-free.

**Why a race condition guard in services?**
HTTP responses can arrive out of order when the user switches context quickly. Each service tracks the last requested ID and discards responses that no longer match — preventing stale data from overwriting the current state.

**Why glassmorphism for the sidebar?**
`backdrop-filter: blur()` with a semi-transparent background creates visual depth without heavy colour contrast. The sidebar floats over the content rather than pushing it, which gives the layout a more modern and spacious feel.

## 🔭 Future Improvements

- Route-level loading skeletons
- Reports feature (pending backend JasperReports integration)
- E2E tests with Playwright
- Docker Compose integration with backend

## 👩‍💻 Author

Camila G. K. Santos — Frontend Developer

LinkedIn: https://www.linkedin.com/in/camilagksantos

This project is part of a portfolio series focused on modern frontend architecture and Angular best practices.