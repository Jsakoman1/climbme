# Adopt the capability documentation standard

This guide is for a separate, explicitly selected project after its owner has
approved a project-local adoption plan. It is not a migration command and Dora
does not discover, read or alter a consumer repository through this guide.

1. Pin and review the selected Dora release, then work only in a clean project
   worktree.
2. Create a project-local plan from the adoption template. Record its baseline,
   required paths, leaf validation and rollback boundary.
3. Add an explicit `capability_inventory` control, current capability inventory,
   system-map links and documentation-evidence claims based on existing project
   documents and evidence. Do not infer historical verification.
4. Run project Doctor, inventory/documentation checks and project-specific
   validation. Commit only the project-owned reviewed change.

The inventory is a current declared view. The consumer retains authority over
its product/domain documents, decisions, plans, task lifecycle, evidence and
verified status. IDC remains opt-in and advisory-only. Roll back an uncommitted
adoption by restoring the worktree, or revert the isolated adoption commit after
commit; do not edit Dora's source history to roll back a consumer.
