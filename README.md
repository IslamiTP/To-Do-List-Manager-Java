This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

# Java To-Do List Manager

**Techstack**: Java, File I/O, JSON, Plain Text

- Developed a command-line application to manage tasks efficiently, allowing users to create, view, update, and delete tasks with a unique identifier and various attributes, including priority, status, due date, and description.
- Implemented unique ID generation, priority, and status categorization for better task organization.
- Utilized file persistence to save and retrieve task data upon application startup, ensuring data is retained between sessions.
- Included task filtering and search features, enabling users to locate tasks by ID, status, priority, due date, or keywords.

**Features**:
- **Add a Task**: User can create a task with ID, title, description, due date, priority, and status.
- **View Tasks**: Display all tasks with filter options for ID, status, priority, or due date.
- **Delete a Task**: Remove tasks by unique identifier.
- **Update a Task**: Modify task details, such as marking as completed or updating the due date.
- **Search Tasks**: Locate tasks using keywords in the title or description.
- **Persistence**: Save tasks to a file format and load them when the application starts.

## Getting Started

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.js`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/basic-features/font-optimization) to automatically optimize and load Inter, a custom Google Font.

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js/) - your feedback and contributions are welcome!

## Deploy on Vercel

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) from the creators of Next.js.

Check out our [Next.js deployment documentation](https://nextjs.org/docs/deployment) for more details.