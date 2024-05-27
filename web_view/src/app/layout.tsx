"use client";

import "./globals.css";
import { createContext, useContext, useEffect, useState } from "react";

interface AccountType {
	name: string;
	token: string;
	setAccount: (name: string, token: string) => void;
}

const AccountContext = createContext<AccountType | null>(null);
export const useAccountContext = () => useContext(AccountContext)!!;

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	const [name, setName] = useState(() => {
		if (true) {
			return localStorage.getItem("name") || "";
		}
		return "";
	});

	const [token, setToken] = useState(() => {
		if (true) {
			return localStorage.getItem("token") || "";
		}
		return "";
	});

	useEffect(() => {
		localStorage.setItem("name", name);
	}, [name]);

	useEffect(() => {
		localStorage.setItem("token", token);
	}, [token]);

	const setAccount = (newName: string, newToken: string) => {
		setName(newName);
		setToken(newToken);
	};

	return (
		<html lang="en">
			<body>
				<AccountContext.Provider value={{ name, token, setAccount }}>
					<div suppressHydrationWarning={true}>{children}</div>
				</AccountContext.Provider>
			</body>
		</html>
	);
}
