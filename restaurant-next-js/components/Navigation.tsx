"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

export default function Navigation({
  isAuthenticated,
  role
}: {
  isAuthenticated: boolean,
  role: string
}) {
  return (
    <nav>
      {role !== "waiter" && (
        <>
          <NavLink href="/restaurant">Main page</NavLink>
          <NavLink href="/book">Book a Table</NavLink>
        </>
      )}
      {isAuthenticated && (
        <NavLink href="/reservations">Reservations</NavLink>
      )}
            {role === "waiter" && (
          <NavLink href="/menu">Menu</NavLink>
      )}
    </nav>
  );
}

function NavLink({ href, children }: { href: string; children: React.ReactNode }) {
  const pathname = usePathname();
  const isActive = pathname === href;

  return (
    <Link href={href} className={`${isActive ? "active" : ""}`}>
      {children}
    </Link>
  );
}