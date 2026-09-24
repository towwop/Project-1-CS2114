# CS2114 Project 1: Terminal Task Manager

Java 17. Eclipse project rooted at `src/`, sources in `src/src/` (default package).

## Team
- Oscar, Abdullah, Vihaan. All three of us work on every file; no per-file ownership.
- Before editing a file someone else is actively working on, check with them first.
- Tests still have one owner per class (see Testing).

## Classes (only these; no extra interfaces or abstract classes)
Main, TUI, TaskStorage, Task, SubTask, StorageFullException.

## Rules
- TaskStorage holds at most 25 tasks (`MAX_TASKS`) in one `Task[]` containing both Tasks and SubTasks.
- `add` / `addSubTask` throw `StorageFullException` (extends `Exception`) when full.
- TaskStorage assigns ids. Ids are never reused.
- Dates use `java.time.LocalDate`. User date input format is `MM/DD/YYYY`, parsed in TUI.
- camelCase fields.
- Input reading, validation, and re-prompting happen only in TUI. Task and SubTask never read input.
  Model classes may reject bad values by returning false/null.
- SubTask extends Task and adds `parentTaskId`. A subtask's parent must exist and cannot be a subtask.
- Removing a parent Task also removes all of its SubTasks (cascade).
- TUI prints items via `toString()` (overridden in SubTask), never `instanceof`.
- All fields private except `public static final` constants.

## Stub conventions
- Every unfinished method body contains `// not implemented` (on the return line, or its own line for void/constructors).
  Search for it to find remaining work. When implementing a method, delete both its `// not implemented` and its `// TODO:`.
- Brace style: opening brace on its own line (CS2114 Eclipse formatter). Match it.

## Git workflow
- We work directly on `main`. Run `git pull --ff-only` before starting work.
- Run the compile check before any push.
- Each of us commits and pushes manually. Claude never commits or pushes; when work is ready, it says so and suggests a commit message.
- Before a large change, Claude says so first so you can commit the current state yourself.
- Never force-push, rebase, or rewrite history.
- `bin/` and `.DS_Store` are gitignored; don't commit build output.
- Compile check: `javac --release 17 -d /tmp/p1build src/src/*.java` from the repo root.

## Testing
Framework: TBD, likely `student.TestCase` (Web-CAT student.jar). Not on the classpath yet. No tests written yet.

Test owners (from the spec's Division of work):
- TaskStorageTest: Oscar
- TaskTest, SubTaskTest: Vihaan
- TUITest: Abdullah

## Deliberate deviations from the spec PDF
- `integer` -> `int`; missing return types -> `void`.
- `java.util.Date` -> `LocalDate`; `Task.getDate()` returns `LocalDate`, not `String`.
- Added `Task.getId()` and `Task.toString()` (overridden in SubTask).
- Added `TaskStorage.getSize()` and `getTasks()` so TUI can list tasks.
- `add(Task)` -> `Task add(String, LocalDate)`; added `Task addSubTask(String, LocalDate, int)`.
- `TUI.handleUserAction()` returns `boolean` (false = quit) so Main can exit.
- Added `StorageFullException(String message)` constructor.
- SubTask constructor takes a 4th arg: `SubTask(int, String, LocalDate, int parentTaskId)`.
- Main uses a local `TUI` variable in `main` instead of a `tui` field.
- snake_case field names -> camelCase (`new_task_id` -> `newTaskId`, `due_date` -> `dueDate`, `parent_task_id` -> `parentTaskId`).
- Validation moved: date-format checking (spec: Task) is in TUI; parent-id checking (spec: SubTask) is in TaskStorage (returns null) with TUI re-prompting.
