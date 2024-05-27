"use client";

import { CreateProjectModal } from "./modal/createProjectModal";
import { CreateUserModal } from "./modal/createUserModal";
import { LoginModal } from "./modal/loginModal";

export function Navbar() {
	return (
		<nav className="bg-stone-50 shadow">
			<div className="container mx-auto px-6 py-3 md:flex md:justify-between md:items-center">
				<div className="flex justify-between items-center">
					<div>
						<a
							className="transition-colors ease-out text-gray-800  text-xl font-extralight md:text-2xl hover:text-blue-500 "
							href="/"
						>
							IssueTracker
						</a>
					</div>
				</div>

				<div className="md:flex items-center">
					<div className="flex justify-center md:block">
						<CreateProjectModal />
						<CreateUserModal />
						<LoginModal />
					</div>
				</div>
			</div>
		</nav>
	);
}
