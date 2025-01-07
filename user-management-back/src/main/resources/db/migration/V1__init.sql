CREATE TABLE public.profiles (
    description character varying(255),
    id character varying(255) NOT NULL,
    name character varying(255)
);

CREATE TABLE public.users (
    birth_date date,
    email character varying(255),
    id character varying(255) NOT NULL,
    name character varying(255),
    phone character varying(255),
    profile_id character varying(255),
    secret character varying(255),
    username character varying(255)
);